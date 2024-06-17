package CentralSync.demo.repository;

import CentralSync.demo.dto.LowStockItemDTO;
import CentralSync.demo.dto.RecentlyUsedItemDTO;
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

//    @Query("SELECT new CentralSync.demo.dto.RecentlyUsedItemDTO(i.itemId, i.itemName, SUM(s.outQty)) " +
//            "FROM InventoryItem i JOIN StockOut s ON i.itemId = s.itemId " +
//            "WHERE  MONTH(s.date) = MONTH(CURRENT_DATE) AND YEAR(s.date) = YEAR(CURRENT_DATE) " +
//            "GROUP BY i.itemId, i.itemName " +
//            "HAVING SUM(s.outQty) > 5 " +
//            "ORDER BY SUM(s.outQty) DESC")
//    List<RecentlyUsedItemDTO> findRecentlyUsedItems();


}


