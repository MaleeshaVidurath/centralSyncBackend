package CentralSync.demo.repository;
import CentralSync.demo.Model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request,Long> {
}
