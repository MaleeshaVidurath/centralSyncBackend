package CentralSync.demo.controller;

import CentralSync.demo.Model.InventoryItem;
import CentralSync.demo.exception.InventoryItemNotFoundException;
import CentralSync.demo.repository.InventoryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InventoryItemController {
    @Autowired
    private InventoryItemRepository inventoryItemRepository;


    @PostMapping("/inventoryItem")
    InventoryItem newInventoryItem(@RequestBody InventoryItem newInventoryItem){
        return inventoryItemRepository.save(newInventoryItem);
    }

    @GetMapping("/inventoryItems")
    List<InventoryItem> getAllInventoryItems(){
        return inventoryItemRepository.findAll();
    }

    @GetMapping("/inventoryItem/{itemId}")
    InventoryItem getInventoryItemById(@PathVariable long itemId){
        return inventoryItemRepository.findById(itemId)
                .orElseThrow(()-> new InventoryItemNotFoundException(itemId));
    }

    @PutMapping("/inventoryItem/{itemId}")
    InventoryItem updateInventoryItem(@RequestBody InventoryItem newInventoryItem, @PathVariable long itemId) {
        return inventoryItemRepository.findById(itemId)
                .map(inventoryItem -> {
                    inventoryItem.setItemName(newInventoryItem.getItemName());
                    inventoryItem.setItemGroup(newInventoryItem.getItemGroup());
                    inventoryItem.setUnit(newInventoryItem.getUnit());
                    inventoryItem.setDimension(newInventoryItem.getDimension());
                    inventoryItem.setWeight(newInventoryItem.getWeight());
                    inventoryItem.setDescription(newInventoryItem.getDescription());
                    inventoryItem.setQuantity(newInventoryItem.getQuantity());

                    return inventoryItemRepository.save(inventoryItem);
                })
                .orElseThrow(() -> new InventoryItemNotFoundException(itemId));
    }
    @DeleteMapping("/inventoryItem/{itemId}")
    String deleteInventoryItem ( @PathVariable long itemId){
        if (!inventoryItemRepository.existsById(itemId)) {
            throw new InventoryItemNotFoundException(itemId);
        }
        inventoryItemRepository.deleteById(itemId);
        return "Inventory Item with id " + itemId + " has been deleted success";
    }
}


