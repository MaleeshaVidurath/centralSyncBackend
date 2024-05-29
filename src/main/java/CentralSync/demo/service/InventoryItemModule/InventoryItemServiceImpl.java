package CentralSync.demo.service.InventoryItemModule;

import CentralSync.demo.exception.InventoryItemModule.InventoryItemNotFoundException;
import CentralSync.demo.model.InventoryItemModule.InventoryItem;
import CentralSync.demo.model.InventoryRequestModule.StatusEnum;
import CentralSync.demo.repository.InventoryItemModule.InventoryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryItemServiceImpl implements InventoryItemService {
    @Autowired
    private InventoryItemRepository inventoryItemRepository;


    @Override
    public InventoryItem saveItem(InventoryItem inventoryItem) {

        return inventoryItemRepository.save(inventoryItem);
    }

    @Override
    public List<InventoryItem> getAllItems() {
        return inventoryItemRepository.findAll();
    }

    @Override
    public InventoryItem getItemById(long itemId) {
        return inventoryItemRepository.findById(itemId)
                .orElseThrow(() -> new InventoryItemNotFoundException(itemId));
    }


    @Override
    public InventoryItem updateItemById(InventoryItem newInventoryItem, long itemId) {
        return inventoryItemRepository.findById(itemId)
                .map(inventoryItem -> {
                    inventoryItem.setItemName(newInventoryItem.getItemName());
                    inventoryItem.setItemGroup(newInventoryItem.getItemGroup());
                    inventoryItem.setBrand(newInventoryItem.getBrand());
                    inventoryItem.setUnit(newInventoryItem.getUnit());
                    inventoryItem.setDimension(newInventoryItem.getDimension());
                    inventoryItem.setWeight(newInventoryItem.getWeight());
                    inventoryItem.setDescription(newInventoryItem.getDescription());
                    inventoryItem.setQuantity(newInventoryItem.getQuantity());
                    inventoryItem.setStatus(newInventoryItem.getStatus());

                    return inventoryItemRepository.save(inventoryItem);
                })
                .orElseThrow(() -> new InventoryItemNotFoundException(itemId));
    }

    @Override
    public InventoryItem updateItemStatus(long itemId) {
        return inventoryItemRepository.findById(itemId)
                .map(inventoryItem -> {
                    inventoryItem.setStatus(StatusEnum.inactive);
                    return inventoryItemRepository.save(inventoryItem);
                })
                .orElseThrow(() -> new InventoryItemNotFoundException(itemId));
    }


    @Override
    public String deleteItemById(long itemId) {
        if (!inventoryItemRepository.existsById(itemId)) {
            throw new InventoryItemNotFoundException(itemId);
        }
        inventoryItemRepository.deleteById(itemId);
        return "Inventory Item with id " + itemId + "deleted successfully";
    }


    //@Override
    //public InventoryItem findByItemNameAndBrand(String itemName, String brand) {
        //return inventoryItemRepository.findByItemNameAndBrand(itemName, brand);
   // }

    @Override
    public int getCountOfInventoryItems() {
        return inventoryItemRepository.countInventoryItem();
    }

    @Override
    public int getCountOfLowStock() {
        return inventoryItemRepository.countLowStock();
    }

}

