package CentralSync.demo.repository;

import CentralSync.demo.model.Adjustment;
import CentralSync.demo.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import CentralSync.demo.model.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    boolean existsByItemId(long itemId);
    List<Reservation> findByUserId(Long userId);

    @Query("SELECT COUNT(a) FROM Reservation a WHERE a.status = 'PENDING'")
    long countPendingReservation();

    Long countByStatusAndUserId(Status status, Long userId);
}
