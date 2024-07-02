package CentralSync.demo.repository;

import CentralSync.demo.dto.LowStockItemDTO;
import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.ItemGroupEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    InventoryItem findByItemNameAndBrand(String itemName, String brand);
    List<InventoryItem > findAllByItemGroup(ItemGroupEnum itemGroup);
    List<InventoryItem> findByItemName(String itemName);

    @Query("SELECT COUNT(i) FROM InventoryItem i")
    int countInventoryItem();

    @Query("SELECT COUNT(i) FROM InventoryItem i WHERE i.quantity <= 5")
    int countLowStock();

    @Query("SELECT new CentralSync.demo.dto.LowStockItemDTO(i.itemId, i.itemName, i.quantity) FROM InventoryItem i WHERE i.quantity <= 5")
    List<LowStockItemDTO> findLowStockItems();

    @Query("SELECT i FROM InventoryItem i WHERE i.itemGroup = :itemGroup AND i.quantity <= 5")
    List<InventoryItem> findLowStockItemsByGroup(ItemGroupEnum itemGroup);

    @Query("SELECT i FROM InventoryItem i WHERE i.quantity <= 5")
    List<InventoryItem> findAllLowStockItems();


}


