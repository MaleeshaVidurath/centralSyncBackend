package CentralSync.demo.repository;

import CentralSync.demo.Model.StockOut;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockOutRepository extends JpaRepository<StockOut,Long> {
}

