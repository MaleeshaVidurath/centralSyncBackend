package CentralSync.demo.controller;

import CentralSync.demo.dto.LowStockItemDTO;
import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.StatusEnum;
import CentralSync.demo.repository.InventoryItemRepository;
import CentralSync.demo.service.InventoryItemService;
import CentralSync.demo.service.LoginService;
import CentralSync.demo.service.UserActivityLogService;
import CentralSync.demo.util.ItemGroupUnitMapping;
import jakarta.validation.Valid;
import org.apache.commons.io.FileUtils;
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
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/inventory-item")
@CrossOrigin(origins = "http://localhost:3000")
public class InventoryItemController {

    @Autowired
    private InventoryItemService inventoryItemService;
    @Autowired
    private UserActivityLogService userActivityLogService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestPart("item") @Valid InventoryItem inventoryItem,
                                 @RequestPart(value = "image", required = false) MultipartFile image,
                                 BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        if (bindingResult.hasErrors()) {
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
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
            }
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }


        inventoryItem.setStatus(StatusEnum.ACTIVE);
        InventoryItem item = inventoryItemService.saveItem(inventoryItem);
        // Log the user activity for the update
Long actorId = loginService.userId;
 userActivityLogService.logUserActivity(actorId, item.getItemId(), "New item Added");

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
        List<InventoryItem> items = inventoryItemService.getAllItems();
        if (items != null) {
            return ResponseEntity.status(HttpStatus.OK).body(items);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping("/getById/{itemId}")
    public ResponseEntity<?> getById(@PathVariable long itemId) {
        InventoryItem item = inventoryItemService.getItemById(itemId);
        if (item != null) {
            return ResponseEntity.status(HttpStatus.OK).body(item);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }


    @PutMapping("/updateById/{itemId}")
    public ResponseEntity<?> updateItem(@RequestBody @Valid InventoryItem newInventoryItem, BindingResult bindingResult, @PathVariable long itemId) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        InventoryItem item = inventoryItemService.updateItemById(newInventoryItem, itemId);
        // Log the user activity for the update
        Long actorId = loginService.userId;
        userActivityLogService.logUserActivity(actorId, item.getItemId(), "Item details updated");
        return ResponseEntity.status(HttpStatus.OK).body("Details were edited successfully");
    }


    @PatchMapping("/updateStatus/{itemId}")
    public ResponseEntity<?> updateStatus(@PathVariable long itemId) {
        try {
            InventoryItem status = inventoryItemService.updateItemStatus(itemId);
            // Log user activity
            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId, status.getItemId(), "Item marked as inactive");
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/deleteItem/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable long itemId) {
        try {
            String result = inventoryItemService.deleteItemById(itemId);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchItem(@RequestParam String itemName, @RequestParam(required = false) List<ItemGroupEnum> itemGroup) {
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
                        e.printStackTrace();
                        // Handle file read error
                    }
                }

                responseItems.add(responseItem);
            }

            return ResponseEntity.status(HttpStatus.OK).body(responseItems);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/count") // get number of inventory items
    public long getInventoryItemCount() {
        return inventoryItemService.getCountOfInventoryItems();
    }

    @GetMapping("/low-count") // get number of low items
    public long getLowItemCount() {
        return inventoryItemService.getCountOfLowStock();
    }

    @GetMapping("/low-stock-items")
    public List<LowStockItemDTO> getLowStockItems() {
        return inventoryItemService.getLowStockItems();
    }

//    @GetMapping("/recently-used-items")
//    public List<RecentlyUsedItemDTO> getRecentlyUsedItems() {
//        return inventoryItemRepository.findRecentlyUsedItems();
//    }

}

