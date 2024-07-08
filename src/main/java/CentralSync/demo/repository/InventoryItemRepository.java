package CentralSync.demo.repository;

import CentralSync.demo.dto.LowStockItemDTO;
import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.ItemGroupEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    InventoryItem findByItemNameAndBrand(String itemName, String brand);

    List<InventoryItem> findAllByItemNameContainingIgnoreCase(String itemName);

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


    @Query("SELECT i FROM InventoryItem i WHERE i.itemName = :itemName AND i.brand = :brand AND i.dimension = :dimension " +
            " AND i.itemGroup = :itemGroup AND " +
            "(i.specification IS NULL OR i.specification = :specification)")
    InventoryItem findDuplicate(@Param("itemName") String itemName,
                                @Param("brand") String brand,
                                @Param("dimension") String dimension,
                                @Param("itemGroup") ItemGroupEnum itemGroup,
                                @Param("specification") String specification);
}


