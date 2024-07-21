package CentralSync.demo.repository;

import CentralSync.demo.dto.MonthlyStockData;
import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.StockIn;
import CentralSync.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockInRepository extends JpaRepository<StockIn,Long> {



    List<StockIn> findAllByItemId_ItemGroup(ItemGroupEnum itemGroup);
    @Query("SELECT s FROM StockIn s WHERE FUNCTION('YEAR', s.date) = :year")
    List<StockIn> stockInByYear(@Param("year") int year);

    @Query("SELECT new CentralSync.demo.dto.MonthlyStockData(MONTH(s.date), SUM(s.inQty)) FROM StockIn s WHERE YEAR(s.date) = :year GROUP BY MONTH(s.date)")
    List<MonthlyStockData> findMonthlyStockIn(@Param("year") int year);

    @Query("SELECT SUM(si.inQty) FROM StockIn si WHERE si.itemId = :item")
    Integer findTotalStockIn(@Param("item") InventoryItem item);

    List<StockIn> findByUserId(User loggedUser);
}
