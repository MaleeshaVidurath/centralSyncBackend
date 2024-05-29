package CentralSync.demo.repository.ReservationModule;
import CentralSync.demo.model.ReservationModule.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
}
