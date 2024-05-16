package CentralSync.demo.controller;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import CentralSync.demo.service.UserActivityLogService;



@RestController

@RequestMapping("/inventory-item")
@CrossOrigin(origins = "http://localhost:3000")
public class InventoryItemController {

    @Autowired
    private InventoryItemService inventoryItemService;
    @Autowired
    private UserActivityLogService userActivityLogService;


    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid InventoryItem inventoryItem, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }

        inventoryItem.setStatus(ItemStatus.ACTIVE);
        InventoryItem item=inventoryItemService.saveItem(inventoryItem);
        // Log the user activity for the update
        userActivityLogService.logUserActivity(item.getItemId(), "New Item Added");
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

        InventoryItem item=inventoryItemService.updateItemById(newInventoryItem, itemId);
        // Log the user activity for the update
        userActivityLogService.logUserActivity(item.getItemId(), "Item Updated");
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

