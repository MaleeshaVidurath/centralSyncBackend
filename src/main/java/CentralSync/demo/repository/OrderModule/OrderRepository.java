package CentralSync.demo.repository.OrderModule;
import CentralSync.demo.model.OrderModule.ItemOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<ItemOrder,Long> {
}
