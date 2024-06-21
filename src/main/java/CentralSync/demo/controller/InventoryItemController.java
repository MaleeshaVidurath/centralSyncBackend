package CentralSync.demo.controller;

import CentralSync.demo.dto.LowStockItemDTO;
import CentralSync.demo.exception.InventoryItemInUseException;
import CentralSync.demo.exception.InventoryItemNotFoundException;
import CentralSync.demo.model.*;
import CentralSync.demo.repository.InventoryItemRepository;
import CentralSync.demo.service.InventoryItemService;
import CentralSync.demo.service.LoginService;
import CentralSync.demo.service.UserActivityLogService;
import CentralSync.demo.util.ItemGroupUnitMapping;
import jakarta.validation.Valid;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/inventory-item")
@CrossOrigin(origins = "http://localhost:3000")
public class InventoryItemController {
    private static final Logger logger = LoggerFactory.getLogger(InventoryItemController.class);
    private final InventoryItemService inventoryItemService;
    private final UserActivityLogService userActivityLogService;
    private final LoginService loginService;
    private final InventoryItemRepository inventoryItemRepository;

    @Autowired
    public InventoryItemController(
            InventoryItemService inventoryItemService,
            UserActivityLogService userActivityLogService,
            LoginService loginService,
            InventoryItemRepository inventoryItemRepository
    ) {
        this.inventoryItemService = inventoryItemService;
        this.userActivityLogService = userActivityLogService;
        this.loginService = loginService;
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestPart("item") @Valid InventoryItem inventoryItem,
                                 @RequestPart(value = "image", required = false) MultipartFile image,
                                 BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors for inventory item: {}", inventoryItem.getItemName());
            errors.putAll(bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
        }
        // Validate the unit based on itemGroup
        if (!isValidUnitForItemGroup(inventoryItem.getItemGroup(), inventoryItem.getUnit())) {
            errors.put("unit", "Invalid unit for the selected item category");
        }

        if (image != null && !image.isEmpty()) {
            try {
                byte[] bytes = image.getBytes();
                Path path = Paths.get("uploads/" + image.getOriginalFilename());
                Files.write(path, bytes);
                inventoryItem.setFilePath(path.toString());
            } catch (IOException e) {
                logger.error("Image upload failed for item: {}", inventoryItem.getItemName(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
            }
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }


        inventoryItem.setStatus(StatusEnum.ACTIVE);
        InventoryItem item = inventoryItemService.saveItem(inventoryItem);
        // Log the user activity for the update
//        Long actorId = loginService.userId;
//        userActivityLogService.logUserActivity(actorId, item.getItemId(), "New Item Added");
        logger.info("Item added to the inventory: {}", inventoryItem.getItemName());
        return ResponseEntity.status(HttpStatus.CREATED).body("Item added to the inventory.");
    }

    private boolean isValidUnitForItemGroup(ItemGroupEnum itemGroup, String unit) {
        if (itemGroup != null && unit != null) {
            Set<String> validUnits = ItemGroupUnitMapping.VALID_UNITS.get(itemGroup);
            return validUnits != null && validUnits.contains(unit);
        }
        return true;
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
                return ResponseEntity.status(HttpStatus.OK).body(item);
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
    public ResponseEntity<?> updateItem(@RequestBody @Valid InventoryItem newInventoryItem, BindingResult bindingResult, @PathVariable long itemId) {
        logger.info("Updating inventory item by ID: {}", itemId);
        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors for inventory item: {}", newInventoryItem.getItemName());
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        InventoryItem item = inventoryItemService.updateItemById(newInventoryItem, itemId);
        logger.info("Updated inventory item: {}", item.getItemName());        // Log the user activity for the update
        Long actorId = loginService.userId;
        userActivityLogService.logUserActivity(actorId, item.getItemId(), "Item details updated");
        return ResponseEntity.status(HttpStatus.OK).body("Details were edited successfully");
    }


    @PatchMapping("/updateStatus/{itemId}")
    public ResponseEntity<?> updateStatus(@PathVariable long itemId) {
        logger.info("Updating status of inventory item by ID: {}", itemId);
        try {
            InventoryItem status = inventoryItemService.updateItemStatus(itemId);
            logger.info("Updated status of inventory item: {}", status.getItemName());
            // Log user activity
            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId, status.getItemId(), "Item marked as inactive");
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
                items = inventoryItemService.getItemByItemName(itemName, itemGroup.toArray(new ItemGroupEnum[0]));
            } else {
                items = inventoryItemService.getItemByItemName(itemName);
            }

            List<Map<String, Object>> responseItems = new ArrayList<>();
            for (InventoryItem item : items) {
                Map<String, Object> responseItem = new HashMap<>();
                responseItem.put("itemId", item.getItemId());
                responseItem.put("itemName", item.getItemName());
                responseItem.put("itemGroup", item.getItemGroup());
                responseItem.put("brand", item.getBrand());
                responseItem.put("quantity", item.getQuantity());
                responseItem.put("description", item.getDescription());
                responseItem.put("status", item.getStatus());

                if (item.getFilePath() != null) {
                    try {
                        byte[] fileContent = FileUtils.readFileToByteArray(new File(item.getFilePath()));
                        String base64Image = Base64.getEncoder().encodeToString(fileContent);
                        responseItem.put("image", base64Image);
                    } catch (IOException e) {
                        logger.error("Error reading image file for item: {}", item.getItemName(), e);

                    }
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

//    @GetMapping("/recently-used-items")
//    public List<RecentlyUsedItemDTO> getRecentlyUsedItems() {
//        return inventoryItemRepository.findRecentlyUsedItems();
//    }

}

