package CentralSync.demo.repository;

import CentralSync.demo.dto.MonthlyStockData;
import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.StockOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockOutRepository extends JpaRepository<StockOut,Long> {


    List<StockOut> findAllByItemId_ItemGroup(ItemGroupEnum itemGroup);
    @Query("SELECT s FROM StockOut s WHERE FUNCTION('YEAR', s.date) = :year")
    List<StockOut> stockOutByYear(@Param("year") int year);

    @Query("SELECT new CentralSync.demo.dto.MonthlyStockData(MONTH(s.date), SUM(s.outQty)) FROM StockOut s WHERE YEAR(s.date) = :year GROUP BY MONTH(s.date)")
    List<MonthlyStockData> findMonthlyStockOut(@Param("year") int year);

    @Query("SELECT i.itemId, SUM(s.outQty) as totalStockOut, i.itemName " +
            "FROM StockOut s JOIN InventoryItem i ON s.itemId.itemId = i.itemId " +
            "WHERE YEAR(s.date) = YEAR(CURRENT_DATE) AND MONTH(s.date) = MONTH(CURRENT_DATE) " +
            "GROUP BY i.itemId, i.itemName " +
            "ORDER BY totalStockOut DESC")
    List<Object[]> findRecentlyUsedItems();

    @Query("SELECT SUM(so.outQty) FROM StockOut so WHERE so.itemId = :item")
    Integer findTotalStockOut(@Param("item") InventoryItem item);
}

