package CentralSync.demo.controller;

import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.ItemStatus;
import CentralSync.demo.service.InventoryItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.converter.HttpMessageNotReadableException;


@RestController

@RequestMapping("/inventory-item")
@CrossOrigin
public class InventoryItemController {

    @Autowired
    private InventoryItemService inventoryItemService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid InventoryItem inventoryItem, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }

        inventoryItem.setStatus(ItemStatus.ACTIVE);
        inventoryItemService.saveItem(inventoryItem);
        return ResponseEntity.ok("New item is added");
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
        return ResponseEntity.ok("Item is edited");
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

