package CentralSync.demo.repository;

import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.StockIn;
import CentralSync.demo.model.StockOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface StockOutRepository extends JpaRepository<StockOut,Long> {
    List<StockOut> findAllByItemIdIn(List<Long> itemIds);

    @Query("SELECT s FROM StockOut s WHERE FUNCTION('YEAR', s.date) = :year")
    List<StockOut> stockOutByYear(@Param("year") int year);
}

