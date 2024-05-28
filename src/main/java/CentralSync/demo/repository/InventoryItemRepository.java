package CentralSync.demo.repository;

import CentralSync.demo.dto.LowStockItemDTO;
import CentralSync.demo.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    InventoryItem findByItemNameAndBrand(String itemName, String brand);

    @Query("SELECT COUNT(i) FROM InventoryItem i")
    int countInventoryItem();

    @Query("SELECT COUNT(i) FROM InventoryItem i WHERE i.quantity <= 5")
    int countLowStock();

//    @Query("SELECT new CentralSync.demo.dto.LowStockItemDTO(i.itemId, i.itemName, COALESCE(SUM(si.inQty), 0), COALESCE(SUM(so.outQty), 0), i.quantity) " +
//            "FROM InventoryItem i " +
//            "LEFT JOIN StockIn si ON i.itemId = si.itemId " +
//            "LEFT JOIN StockOut so ON i.itemId = so.itemId " +
//            "GROUP BY i.itemId, i.itemName, i.quantity " +
//            "HAVING i.quantity <= 5")
//    List<LowStockItemDTO> findLowStockItems();

    @Query("SELECT new CentralSync.demo.dto.LowStockItemDTO(i.itemId, i.itemName, COALESCE(SUM(si.inQty), 0), COALESCE(SUM(so.outQty), 0), i.quantity) " +
            "FROM InventoryItem i " +
            "LEFT JOIN StockIn si ON i.itemId = si.itemId " +
            "LEFT JOIN StockOut so ON i.itemId = so.itemId " +
            "GROUP BY i.itemId, i.itemName, i.quantity " +
            "HAVING i.quantity <= 5")
    List<LowStockItemDTO> findLowStockItems();

}


