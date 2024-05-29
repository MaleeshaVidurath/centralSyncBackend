package CentralSync.demo.repository.TicketModule;
import CentralSync.demo.model.MaintenanceTicketModule.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
}
