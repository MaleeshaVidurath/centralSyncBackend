package CentralSync.demo.repository;

import CentralSync.demo.dto.LowStockItemDTO;
import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.ItemGroupEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    InventoryItem findByItemNameAndBrandAndModel(String itemName, String brand, String model);


    List<InventoryItem> findAllByItemNameContainingIgnoreCase(String itemName);

    @Query("SELECT i FROM InventoryItem i WHERE i.itemName = :itemName AND i.brand = :brand")
    List<InventoryItem> findItemsByItemNameAndBrand(@Param("itemName") String itemName, @Param("brand") String brand);

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


    @Query("SELECT i FROM InventoryItem i WHERE i.itemGroup = :itemGroup AND LOWER(i.brand) = LOWER(:brand) AND LOWER(i.model) = LOWER(:model) ")
    InventoryItem findDuplicate(@Param("itemGroup") ItemGroupEnum itemGroup,
                                @Param("brand") String brand,
                                @Param("model") String model);


    @Query("SELECT i FROM InventoryItem i WHERE i.itemGroup = :itemGroup")
    List<InventoryItem> findByItemGroup(@Param("itemGroup") ItemGroupEnum itemGroup);

    @Query("SELECT i FROM InventoryItem i")
    List<InventoryItem> findAllItems();
}


