package CentralSync.demo.service.InventoryItemModule;

import CentralSync.demo.model.InventoryItemModule.InventoryItem;

import java.util.List;

public interface InventoryItemService {
     InventoryItem saveItem(InventoryItem inventoryItem);
     List<InventoryItem> getAllItems();

     InventoryItem getItemById(long itemId);
     InventoryItem updateItemById(InventoryItem newInventoryItem,long itemId);

      InventoryItem updateItemStatus(long itemId);

     String deleteItemById(long itemId);

     //InventoryItem findByItemNameAndBrand(String itemName, String brand);


     int getCountOfInventoryItems();

     int getCountOfLowStock();
}
