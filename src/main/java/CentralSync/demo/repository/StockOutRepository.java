package CentralSync.demo.repository;

import CentralSync.demo.model.StockOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockOutRepository extends JpaRepository<StockOut,Long> {
}
