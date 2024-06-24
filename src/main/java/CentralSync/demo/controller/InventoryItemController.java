package CentralSync.demo.controller;

import CentralSync.demo.dto.LowStockItemDTO;
import CentralSync.demo.dto.RecentlyUsedItemDTO;
import CentralSync.demo.model.*;
import CentralSync.demo.repository.InventoryItemRepository;
import CentralSync.demo.service.InventoryItemService;
import CentralSync.demo.service.LoginService;
import CentralSync.demo.service.UserActivityLogService;
import CentralSync.demo.util.ItemGroupUnitMapping;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    public ResponseEntity<?> add(@RequestBody @Valid InventoryItem inventoryItem, BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        if (bindingResult.hasErrors()) {
            errors.putAll(bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
        }
        // Validate the unit based on itemGroup
        if (!isValidUnitForItemGroup(inventoryItem.getItemGroup(), inventoryItem.getUnit())) {
            errors.put("unit", "Invalid unit for the selected item category");
        }
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }


        inventoryItem.setStatus(StatusEnum.ACTIVE);
        InventoryItem item = inventoryItemService.saveItem(inventoryItem);
        // Log the user activity for the update
        Long actorId = loginService.userId;
        userActivityLogService.logUserActivity(actorId, item.getItemId(), "New Item Added");

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
            return ResponseEntity.status(HttpStatus.OK).body(items);
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

}

