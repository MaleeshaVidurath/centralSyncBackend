package CentralSync.demo.repository;

import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.StockOut;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StockOutRepository extends JpaRepository<StockOut,Long> {
    List<StockOut> findAllByItemGroup(ItemGroupEnum itemGroup);
    List<StockOut> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
}

