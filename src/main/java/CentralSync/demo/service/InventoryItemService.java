package CentralSync.demo.service;

import CentralSync.demo.dto.LowStockItemDTO;
import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.ItemGroupEnum;

import java.util.List;

public interface InventoryItemService {
    InventoryItem saveItem(InventoryItem inventoryItem);
    List<InventoryItem> getAllItems();
    InventoryItem getItemById(long itemId);
    InventoryItem updateItemById(InventoryItem newInventoryItem, long itemId);
    InventoryItem markAsInactive(long itemId);
    InventoryItem markAsActive(long itemId);
    Boolean isActive(long itemId);
    String deleteItemById(long itemId);
    int getCountOfInventoryItems();
    int getCountOfLowStock();
    List<InventoryItem> getItemByItemName(String itemName, ItemGroupEnum... itemGroup);
    List<LowStockItemDTO> getLowStockItems();
    InventoryItem findDuplicateItem(InventoryItem inventoryItem);
    List<String> getModelNamesByItemNameAndBrand(String itemName, String brand);

}
