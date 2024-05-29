package CentralSync.demo.repository.InventoryItemModule;

import CentralSync.demo.model.InventoryItemModule.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    InventoryItem findByItemNameAndBrand(String itemName, String brand);

    @Query("SELECT COUNT(i) FROM InventoryItem i")
    int countInventoryItem();

    @Query("SELECT COUNT(i) FROM InventoryItem i WHERE i.quantity <= 5")
    int countLowStock();

}


