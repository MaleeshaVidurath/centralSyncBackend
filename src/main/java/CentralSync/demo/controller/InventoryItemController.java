package CentralSync.demo.controller;

import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.StatusEnum;
import CentralSync.demo.service.InventoryItemService;
import CentralSync.demo.util.ItemGroupUnitMapping;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/inventory-item")
@CrossOrigin
public class InventoryItemController {

    @Autowired
    private InventoryItemService inventoryItemService;

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

        inventoryItem.setStatus(StatusEnum.active);
        inventoryItemService.saveItem(inventoryItem);
        return ResponseEntity.ok("New item is added");
    }

    private boolean isValidUnitForItemGroup(ItemGroupEnum itemGroup, String unit) {
        if (itemGroup != null && unit != null) {
            Set<String> validUnits = ItemGroupUnitMapping.VALID_UNITS.get(itemGroup);
            return validUnits != null && validUnits.contains(unit);
        }
        return true;
    }

    @GetMapping("/getAll")
    public List<InventoryItem> list() {
        return inventoryItemService.getAllItems();
    }

    @GetMapping("/getById/{itemId}")
    public InventoryItem listById(@PathVariable long itemId) {
        return inventoryItemService.getItemById(itemId);
    }

    @PutMapping("/updateById/{itemId}")
    public ResponseEntity<?> updateItem(@RequestBody @Valid InventoryItem newInventoryItem, BindingResult bindingResult,@PathVariable long itemId) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }

        inventoryItemService.updateItemById(newInventoryItem, itemId);
        return ResponseEntity.ok("Item details edited");
    }


    @PatchMapping("/updateStatus/{itemId}")
    public InventoryItem updateStatus( @PathVariable long itemId) {
        return inventoryItemService.updateItemStatus(itemId);
    }


    @DeleteMapping("/deleteItem/{itemId}")
    public String deleteItem(@PathVariable long itemId) {
        return inventoryItemService.deleteItemById(itemId);

    }

}

