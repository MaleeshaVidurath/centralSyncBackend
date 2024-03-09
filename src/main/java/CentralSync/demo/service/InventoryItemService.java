package CentralSync.demo.service;
import CentralSync.demo.Model.InventoryItem;
import java.util.*;


public interface InventoryItemService {
    public InventoryItem saveItem(InventoryItem inventoryItem);
    public List<InventoryItem> getAllItems();

    public InventoryItem getItemById(long itemId);
    public InventoryItem updateItemById(InventoryItem newInventoryItem,long itemId);

    public  InventoryItem updateItemStatus(String status, long itemId);

    public String deleteItemById(long itemId);

}
