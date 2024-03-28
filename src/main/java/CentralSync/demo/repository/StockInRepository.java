package CentralSync.demo.repository;

<<<<<<< HEAD
import CentralSync.demo.model.StockIn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface StockInRepository extends JpaRepository<StockIn,Long> {
    List<StockIn> findAllByItemGroup(String itemGroup);
    List<StockIn> findAllByDateContains(String year);

}
=======
<<<<<<<< HEAD:src/main/java/CentralSync/demo/repository/RequestRepository.java
import CentralSync.demo.model.Request;
========
import CentralSync.demo.model.StockIn;
>>>>>>>> origin:src/main/java/CentralSync/demo/repository/StockInRepository.java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockInRepository extends JpaRepository<StockIn,Long> {
}
>>>>>>> origin
