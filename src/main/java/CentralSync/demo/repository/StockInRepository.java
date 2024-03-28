package CentralSync.demo.repository;


import CentralSync.demo.model.StockIn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface StockInRepository extends JpaRepository<StockIn,Long> {
    List<StockIn> findAllByItemGroup(String itemGroup);
    List<StockIn> findAllByDateContains(String year);

}
