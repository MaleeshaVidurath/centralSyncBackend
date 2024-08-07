package CentralSync.demo.controller;

import CentralSync.demo.dto.InventorySummaryDto;
import CentralSync.demo.dto.LowStockItemDTO;
import CentralSync.demo.exception.InventoryItemInUseException;
import CentralSync.demo.exception.InventoryItemNotFoundException;
import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.StatusEnum;
import CentralSync.demo.repository.InventoryItemRepository;
import CentralSync.demo.service.InventoryItemService;
import CentralSync.demo.service.LoginService;
import CentralSync.demo.service.UserActivityLogService;
import CentralSync.demo.service.WSService;
import CentralSync.demo.util.FileUtil;
import CentralSync.demo.util.ItemGroupUnitMapping;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

;


@RestController
@RequestMapping("/inventory-item")
@CrossOrigin(origins = "http://localhost:3000")
public class InventoryItemController {
    private static final Logger logger = LoggerFactory.getLogger(InventoryItemController.class);
    private final InventoryItemService inventoryItemService;
    private final UserActivityLogService userActivityLogService;
    private final LoginService loginService;
    private final InventoryItemRepository inventoryItemRepository;
    private final WSService wsService;

    @Autowired
    public InventoryItemController(
            InventoryItemService inventoryItemService,
            UserActivityLogService userActivityLogService,
            LoginService loginService,
            InventoryItemRepository inventoryItemRepository, WSService wsService
    ) {
        this.inventoryItemService = inventoryItemService;
        this.userActivityLogService = userActivityLogService;
        this.loginService = loginService;
        this.inventoryItemRepository = inventoryItemRepository;
        this.wsService = wsService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestPart("item") @Valid InventoryItem inventoryItem, BindingResult bindingResult,
                                 @RequestPart(value = "image", required = false) MultipartFile image) {
        Map<String, String> errors = new HashMap<>();

        if (bindingResult.hasErrors()) {
            errors.putAll(bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
        }
        // Validate the unit based on itemGroup
        if (isValidUnitForItemGroup(inventoryItem.getItemGroup(), inventoryItem.getUnit())) {
            errors.put("unit", "Invalid unit for the selected item category");
        }

        // Check if the image is provided
        if (image == null || image.isEmpty()) {
            errors.put("image", "Image upload is required");
        }

        if (!errors.isEmpty()) {
            logger.warn("Validation errors for inventory item: {}", inventoryItem.getItemName());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        // Check for duplicate items
        InventoryItem duplicateItem = inventoryItemService.findDuplicateItem(inventoryItem);
        if (duplicateItem != null) {
            logger.warn("Duplicate item found: {}", duplicateItem.getItemName());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(duplicateItem);
        }

        try {
            String imagePath = FileUtil.saveFile(image, image.getOriginalFilename());
            inventoryItem.setImagePath(imagePath);
        } catch (IOException e) {
            logger.error("Image upload failed for item: {}", inventoryItem.getItemName(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
        }


        inventoryItem.setStatus(StatusEnum.ACTIVE);
        InventoryItem item = inventoryItemService.saveItem(inventoryItem);
        // Log the user activity
        Long actorId = loginService.userId;
        userActivityLogService.logUserActivity(actorId, item.getItemId(), "New Item Added");
        logger.info("Item added to the inventory: {}", inventoryItem.getItemName());


        // Send notifications to all users
        String message = "New " + inventoryItem.getItemName() + " has been added to the inventory";
        wsService.notifyFrontend(message);


        return ResponseEntity.status(HttpStatus.CREATED).body("Item added to the inventory.");
    }


    private boolean isValidUnitForItemGroup(ItemGroupEnum itemGroup, String unit) {
        if (itemGroup != null && unit != null) {
            Set<String> validUnits = ItemGroupUnitMapping.VALID_UNITS.get(itemGroup);
            return validUnits == null || !validUnits.contains(unit);
        }
        return false;
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> list() {
        logger.info("Fetching all inventory items.");
        List<InventoryItem> items = inventoryItemService.getAllItems();
        if (items != null) {
            logger.info("Found {} inventory items.", items.size());
            return ResponseEntity.status(HttpStatus.OK).body(items);
        } else {
            logger.warn("No inventory items found.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping("/getById/{itemId}")
    public ResponseEntity<?> getById(@PathVariable long itemId) {
        logger.info("Fetching inventory item by ID: {}", itemId);
        try {
            InventoryItem item = inventoryItemService.getItemById(itemId);
            if (item != null) {
                logger.info("Found inventory item: {}", item.getItemName());
                Map<String, Object> responseItem = new HashMap<>();

                responseItem.put("itemId", item.getItemId());
                responseItem.put("itemName", item.getItemName());
                responseItem.put("itemGroup", item.getItemGroup());
                responseItem.put("brand", item.getBrand());
                responseItem.put("model", item.getModel());
                responseItem.put("unit", item.getUnit());
                responseItem.put("dimension", item.getDimension());
                responseItem.put("weight", item.getWeight());
                responseItem.put("quantity", item.getQuantity());
                responseItem.put("description", item.getDescription());
                responseItem.put("status", item.getStatus());

                if (item.getImagePath() != null) {
                    String base64Image = FileUtil.getFileAsBase64(item.getImagePath());
                    responseItem.put("image", base64Image);
                }
                return ResponseEntity.status(HttpStatus.OK).body(responseItem);
            } else {
                logger.warn("Inventory item with ID {} not found.", itemId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (InventoryItemNotFoundException e) {
            logger.error("Error fetching inventory item by ID: {}", itemId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }


    }


    @PutMapping("/updateById/{itemId}")
    public ResponseEntity<?> updateItem(@PathVariable long itemId,
                                        @RequestPart("item") @Valid InventoryItem newInventoryItem,
                                        BindingResult bindingResult,
                                        @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        logger.info("Updating inventory item by ID: {}", itemId);

        Map<String, String> errors = new HashMap<>();

        if (bindingResult.hasErrors()) {
            errors.putAll(bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
        }

        if (isValidUnitForItemGroup(newInventoryItem.getItemGroup(), newInventoryItem.getUnit())) {
            errors.put("unit", "Invalid unit for the selected item category");
        }
        if (!errors.isEmpty()) {
            logger.warn("Validation errors for inventory item: {}", newInventoryItem.getItemName());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        Optional<InventoryItem> existingItem = Optional.ofNullable(inventoryItemService.getItemById(itemId));
        if (!existingItem.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        InventoryItem duplicateItem = inventoryItemService.findDuplicateItem(newInventoryItem);
        if (duplicateItem != null && duplicateItem.getItemId() != itemId) {
            logger.warn("Duplicate item found: {}", duplicateItem.getItemName());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(duplicateItem);
        }
        if (image != null && !image.isEmpty()) {
            try {
                String filePath = FileUtil.saveFile(image, image.getOriginalFilename());
                newInventoryItem.setImagePath(filePath);
            } catch (IOException e) {
                logger.error("Image upload failed for item: {}", newInventoryItem.getItemName(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
            }
        } else {
            // Preserve the existing image path if no new image is uploaded
            newInventoryItem.setImagePath(existingItem.get().getImagePath());
        }

        InventoryItem item = inventoryItemService.updateItemById(newInventoryItem, itemId);
        logger.info("Updated inventory item: {}", item.getItemName());        // Log the user activity for the update
        Long actorId = loginService.userId;
        userActivityLogService.logUserActivity(actorId, item.getItemId(), "Item details updated");
        return ResponseEntity.status(HttpStatus.OK).body("Details were edited successfully");
    }


    @PatchMapping("/markAsInactive/{itemId}")
    public ResponseEntity<?> markAsInactive(@PathVariable long itemId) {
        try {
            InventoryItem status = inventoryItemService.markAsInactive(itemId);
            logger.info("Updated status of inventory item: {}", status.getItemName());
            // Log user activity
            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId, status.getItemId(), "Item marked as inactive");

            // Send notifications to all users
            String message = status.getItemName() + " has been 'INACTIVE' from the inventory ";
            wsService.notifyFrontend(message);

            return ResponseEntity.ok(status);

        } catch (Exception e) {
            logger.error("Error updating status of inventory item by ID: {}", itemId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PatchMapping("/markAsActive/{itemId}")
    public ResponseEntity<?> markAsActive(@PathVariable long itemId) {
        try {
            InventoryItem status = inventoryItemService.markAsActive(itemId);
            logger.info("Updated status of inventory item: {}", status.getItemName());

            // Log user activity
            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId, status.getItemId(), "Item marked as Active");

            // Send notifications to all users
            String message = status.getItemName() + " now 'ACTIVE' in the inventory";
            wsService.notifyFrontend(message);

            return ResponseEntity.ok(status);


        } catch (Exception e) {
            logger.error("Error updating status of inventory item by ID: {}", itemId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/deleteItem/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable long itemId) {
        logger.info("Deleting inventory item by ID: {}", itemId);
        try {
            String result = inventoryItemService.deleteItemById(itemId);
            logger.info("Deleted inventory item by ID: {}", itemId);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (InventoryItemNotFoundException e) {
            logger.warn("Inventory item not found: {}", itemId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InventoryItemInUseException e) {
            logger.warn("Inventory item in use: {}", itemId, e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error deleting inventory item by ID: {}", itemId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchItem(@RequestParam String itemName, @RequestParam(required = false) List<ItemGroupEnum> itemGroup) {
        logger.info("Searching inventory items by name: {}", itemName);
        try {
            List<InventoryItem> items;
            if (itemGroup != null && !itemGroup.isEmpty()) {
                items = inventoryItemService.searchItems(itemName, itemGroup.toArray(new ItemGroupEnum[0]));
            } else {
                items = inventoryItemService.searchItems(itemName);
            }

            List<Map<String, Object>> responseItems = new ArrayList<>();
            for (InventoryItem item : items) {
                Map<String, Object> responseItem = new HashMap<>();
                responseItem.put("itemId", item.getItemId());
                responseItem.put("itemName", item.getItemName());
                responseItem.put("itemGroup", item.getItemGroup());
                responseItem.put("model", item.getModel());
                responseItem.put("brand", item.getBrand());
                responseItem.put("quantity", item.getQuantity());
                responseItem.put("description", item.getDescription());
                responseItem.put("status", item.getStatus());

                if (item.getImagePath() != null) {
                    String base64Image = FileUtil.getFileAsBase64(item.getImagePath());
                    responseItem.put("image", base64Image);
                }

                responseItems.add(responseItem);
            }
            logger.info("Found {} inventory items matching search criteria.", responseItems.size());
            return ResponseEntity.status(HttpStatus.OK).body(responseItems);
        } catch (Exception e) {
            logger.error("Error searching inventory items by name: {}", itemName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("/count") // get number of inventory items
    public long getInventoryItemCount() {
        logger.info("Fetching count of all inventory items.");
        return inventoryItemService.getCountOfInventoryItems();
    }

    @GetMapping("/low-count") // get number of low items
    public long getLowItemCount() {
        logger.info("Fetching count of low stock inventory items.");
        return inventoryItemService.getCountOfLowStock();
    }

    @GetMapping("/low-stock-items")
    public List<LowStockItemDTO> getLowStockItems() {
        logger.info("Fetching low stock inventory items.");
        return inventoryItemService.getLowStockItems();
    }

    @GetMapping("/grouped")  // get group count for pie chart
    public Map<ItemGroupEnum, Long> getItemGroups() {
        List<InventoryItem> items = inventoryItemRepository.findAll();
        Map<ItemGroupEnum, Long> itemGroupCount = new HashMap<>();
        for (InventoryItem item : items) {
            ItemGroupEnum group = item.getItemGroup();
            itemGroupCount.put(group, itemGroupCount.getOrDefault(group, 0L) + 1);
        }
        return itemGroupCount;
    }

    @GetMapping("/report/low-stock")
    public List<InventoryItem> getLowStockItems(@RequestParam String itemGroup) {
        if ("ALL_ITEM".equals(itemGroup)) {
            return inventoryItemRepository.findAllLowStockItems();
        } else {
            ItemGroupEnum itemGroupEnum;
            try {
                itemGroupEnum = ItemGroupEnum.valueOf(itemGroup);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid item group: " + itemGroup);
            }
            return inventoryItemRepository.findLowStockItemsByGroup(itemGroupEnum);
        }
    }

    @GetMapping("/inventory-summary")
    public List<InventorySummaryDto> getInventorySummary(@RequestParam ItemGroupEnum itemGroup) {
        return inventoryItemService.getInventorySummary(itemGroup);
    }


    @GetMapping("/getBrandsByItemName")
    public ResponseEntity<?> getBrandsByItemName(@RequestParam String itemName) {
        logger.info("Fetching brands by item name: {}", itemName);
        try {
            List<InventoryItem> items = inventoryItemService.searchItems(itemName);
            List<String> brandNames = items.stream()
                    .map(InventoryItem::getBrand)
                    .distinct() // To get unique brand names
                    .collect(Collectors.toList());
            logger.info("Found {} brands for item name: {}", brandNames.size(), itemName);
            return ResponseEntity.status(HttpStatus.OK).body(brandNames);
        } catch (Exception e) {
            logger.error("Error fetching brands for item name: {}", itemName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("/getModelsByItemNameAndBrand")
    public ResponseEntity<?> getModelsByItemNameAndBrand(@RequestParam String itemName, @RequestParam String brand) {
        logger.info("Fetching model names by item name: {} and brand: {}", itemName, brand);
        try {
            List<String> modelNames = inventoryItemService.getModelNamesByItemNameAndBrand(itemName, brand);
            if (modelNames.isEmpty()) {
                logger.warn("No models found for item name: {} and brand: {}", itemName, brand);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            logger.info("Found {} models for item name: {} and brand: {}", modelNames.size(), itemName, brand);
            return ResponseEntity.status(HttpStatus.OK).body(modelNames);
        } catch (Exception e) {
            logger.error("Error fetching models for item name: {} and brand: {}", itemName, brand, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getItemByDetails")
    public ResponseEntity<?> getItemByDetails(
            @RequestParam String itemName,
            @RequestParam String brand,
            @RequestParam String model
    ) {

        try {
            InventoryItem item = inventoryItemRepository.findByItemNameAndBrandAndModel(itemName, brand, model);
            if (item != null) {
                return ResponseEntity.ok(item);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching item details");
        }
    }



}

