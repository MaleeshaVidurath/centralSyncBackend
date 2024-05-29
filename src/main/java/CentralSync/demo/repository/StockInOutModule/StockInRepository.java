package CentralSync.demo.repository.StockInOutModule;


import CentralSync.demo.model.InventoryItemModule.ItemGroupEnum;
import CentralSync.demo.model.StockInStockOutModule.StockIn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StockInRepository extends JpaRepository<StockIn,Long> {
    List<StockIn> findAllByItemGroup(ItemGroupEnum itemGroup);

    List<StockIn> findAllByDateBetween(LocalDate startDate, LocalDate endDate);


}
