package CentralSync.demo.service.MaintenanceTicketModule;

import CentralSync.demo.model.MaintenanceTicketModule.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    public Ticket saveTicket(Ticket ticket);
    public List<Ticket> getAllTickets();

    Optional<Ticket> findById(Long id);
    public Ticket updateTicket(Long id, Ticket newTicket);

    Ticket updateTicketStatusReviewed(long TicketId);

    Ticket updateTicketStatusSENDTOADMIN(long TicketId);

    Ticket updateTicketStatusPENDING(long TicketId);

    String deleteTicket(Long id);

}



