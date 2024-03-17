package CentralSync.demo.repository;

import CentralSync.demo.Model.StockIn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockInRepository extends JpaRepository<StockIn,Long> {
}
