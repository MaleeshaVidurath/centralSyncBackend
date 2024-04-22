package CentralSync.demo.repository;

import CentralSync.demo.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    InventoryItem findByItemNameAndBrand(String itemName, String brand);

}


