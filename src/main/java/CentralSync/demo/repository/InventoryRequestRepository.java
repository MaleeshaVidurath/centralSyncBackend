package CentralSync.demo.repository;

import CentralSync.demo.model.InventoryRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InventoryRequestRepository extends JpaRepository<InventoryRequest, Long> {
    //List<InventoryRequest> findAllByItemGroup(ItemGroupEnum itemGroup);
    List<InventoryRequest> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
    List<InventoryRequest> findByUserUserId(Long userId);


}
