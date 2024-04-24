package CentralSync.demo.repository;

import CentralSync.demo.model.InventoryRequest;
import CentralSync.demo.model.ItemGroupEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface InventoryRequestRepository extends JpaRepository<InventoryRequest, Long> {
    List<InventoryRequest> findAllByItemGroup(ItemGroupEnum itemGroup);
    List<InventoryRequest> findAllByDateBetween(Date startDate,Date endDate);


}
