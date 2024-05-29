package CentralSync.demo.repository.StockInOutModule;

import CentralSync.demo.model.InventoryItemModule.ItemGroupEnum;
import CentralSync.demo.model.StockInStockOutModule.StockOut;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StockOutRepository extends JpaRepository<StockOut,Long> {
    List<StockOut> findAllByItemGroup(ItemGroupEnum itemGroup);
    List<StockOut> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
}

