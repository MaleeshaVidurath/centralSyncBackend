package CentralSync.demo.service;
import CentralSync.demo.model.InventoryItem;
<<<<<<< HEAD

import java.util.*;

=======
import java.util.List;
>>>>>>> origin

public interface InventoryItemService {
    public InventoryItem saveItem(InventoryItem inventoryItem);
    public List<InventoryItem> getAllItems();

    public InventoryItem getItemById(long itemId);
    public InventoryItem updateItemById(InventoryItem newInventoryItem,long itemId);

    public  InventoryItem updateItemStatus(long itemId);

    public String deleteItemById(long itemId);

}
