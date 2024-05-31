package CentralSync.demo.repository;

import CentralSync.demo.model.StockIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface StockInRepository extends JpaRepository<StockIn,Long> {

    List<StockIn> findAllByItemIdIn(List<Long> itemIds);

    @Query("SELECT s FROM StockIn s WHERE FUNCTION('YEAR', s.date) = :year")
    List<StockIn> stockInByYear(@Param("year") int year);

}
