package CentralSync.demo.service;

import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.Ticket;
import CentralSync.demo.model.User;

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



