package CentralSync.demo.repository;


import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.StockIn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface StockInRepository extends JpaRepository<StockIn,Long> {
    List<StockIn> findAllByItemGroup(ItemGroupEnum itemGroup);

    List<StockIn> findAllByDateBetween(LocalDate startDate, LocalDate endDate);


}
