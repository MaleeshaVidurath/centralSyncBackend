package CentralSync.demo.controller;

import CentralSync.demo.Model.InventoryItem;
import CentralSync.demo.service.InventoryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory-item")
@CrossOrigin
public class InventoryItemController {

@Autowired
    private InventoryItemService inventoryItemService;

@PostMapping("/add")
    public InventoryItem add(@RequestBody InventoryItem inventoryItem){
    inventoryItemService.saveItem(inventoryItem);
    return inventoryItem;
}

@GetMapping("/getAll")
    public List<InventoryItem> list(){
    return inventoryItemService.getAllItems();
}

@GetMapping("/getById/{itemId}")
    public InventoryItem listById(@PathVariable long itemId){
    return inventoryItemService.getItemById(itemId);
}

@PutMapping("/updateById/{itemId}")
    public InventoryItem updateItem(@RequestBody InventoryItem newInventoryItem,@PathVariable long itemId){
    return inventoryItemService.updateItemById(newInventoryItem ,itemId);
}

@DeleteMapping("/deleteItem/{itemId}")

    public String deleteItem(@PathVariable long itemId){
    return  inventoryItemService.deleteItemById(itemId);
}

}


