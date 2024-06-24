package CentralSync.demo.repository;

import CentralSync.demo.model.InventoryRequest;
import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRequestRepository extends JpaRepository<InventoryRequest, Long> {
    //List<InventoryRequest> findAllByItemGroup(ItemGroupEnum itemGroup);
    List<InventoryRequest> findByUserUserId(Long userId);

    @Query("SELECT r FROM InventoryRequest r WHERE FUNCTION('YEAR', r.dateTime) = :year")
    List<InventoryRequest> requestsByYear(@Param("year") int year);

    List<InventoryRequest> findAllByInventoryItem_ItemGroup(ItemGroupEnum itemGroup);

    @Query("SELECT COUNT(a) FROM InventoryRequest a WHERE a.reqStatus = 'PENDING'")
    long countPendingRequest();
}
