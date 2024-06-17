package CentralSync.demo.repository;

import CentralSync.demo.dto.MonthlyStockData;
import CentralSync.demo.model.StockOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockOutRepository extends JpaRepository<StockOut,Long> {
    List<StockOut> findAllByItemIdIn(List<Long> itemIds);

    @Query("SELECT s FROM StockOut s WHERE FUNCTION('YEAR', s.date) = :year")
    List<StockOut> stockOutByYear(@Param("year") int year);

    @Query("SELECT new CentralSync.demo.dto.MonthlyStockData(MONTH(s.date), SUM(s.outQty)) FROM StockOut s WHERE YEAR(s.date) = :year GROUP BY MONTH(s.date)")
    List<MonthlyStockData> findMonthlyStockOut(@Param("year") int year);
}

