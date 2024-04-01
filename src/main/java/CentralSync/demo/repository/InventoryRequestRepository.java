package CentralSync.demo.repository;

import CentralSync.demo.model.InventoryRequest;
import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.StockIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRequestRepository extends JpaRepository<InventoryRequest, Long> {


}
