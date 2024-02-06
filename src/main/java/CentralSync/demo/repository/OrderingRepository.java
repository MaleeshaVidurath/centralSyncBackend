package CentralSync.demo.repository;
import CentralSync.demo.Model.Ordering;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderingRepository extends JpaRepository<Ordering,Long> {
}
