package CentralSync.demo.service;

import CentralSync.demo.model.InventoryItem;


import java.util.*;

public interface InventoryItemService {
    InventoryItem saveItem(InventoryItem inventoryItem);

    List<InventoryItem> getAllItems();

    InventoryItem getItemById(long itemId);

    InventoryItem updateItemById(InventoryItem newInventoryItem, long itemId);

    InventoryItem updateItemStatus(long itemId);

    Boolean isActive(long itemId);
    String deleteItemById(long itemId);

    int getCountOfInventoryItems();

    int getCountOfLowStock();
}
