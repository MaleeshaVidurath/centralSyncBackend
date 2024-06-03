package CentralSync.demo.repository;
import CentralSync.demo.model.ItemOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemOrderRepository extends JpaRepository<ItemOrder,Long> {
}
