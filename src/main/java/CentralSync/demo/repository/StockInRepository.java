package CentralSync.demo.repository;

import CentralSync.demo.model.StockIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockInRepository extends JpaRepository<StockIn,Long> {
}
