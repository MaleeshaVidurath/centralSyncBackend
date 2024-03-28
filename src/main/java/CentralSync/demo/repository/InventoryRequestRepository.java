package CentralSync.demo.repository;

import CentralSync.demo.model.InventoryRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRequestRepository extends JpaRepository<InventoryRequest, Long> {
InventoryRequest save(InventoryRequest entity);
}
