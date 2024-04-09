package CentralSync.demo.repository;

import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.StockIn;
import CentralSync.demo.model.StockOut;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockOutRepository extends JpaRepository<StockOut,Long> {
    List<StockOut> findAllByItemGroup(ItemGroupEnum itemGroup);
    List<StockOut> findAllByDateContains(String year);
}

