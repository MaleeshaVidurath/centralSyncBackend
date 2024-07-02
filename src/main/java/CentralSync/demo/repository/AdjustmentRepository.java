package CentralSync.demo.repository;

import CentralSync.demo.model.Adjustment;
import CentralSync.demo.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdjustmentRepository extends JpaRepository<Adjustment, Long> {
    List<Adjustment> findByUserId(Long userId);

    @Query("SELECT COUNT(a) FROM Adjustment a WHERE a.status = 'PENDING'")
    long countPendingAdjustments();

    Long countByStatusAndUserId(Status status, Long userId);
}