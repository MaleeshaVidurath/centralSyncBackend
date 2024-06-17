package CentralSync.demo.service;

import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.Ticket;
import CentralSync.demo.model.UserActivityLog;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TicketService {
    public Ticket saveTicket(Ticket ticket);
    public List<Ticket> getAllTickets();
    List<Ticket> getTicketsByUser(Long userId);
    Optional<Ticket> findById(Long id);
    public Ticket updateTicket(Long id, Ticket newTicket);
    Ticket updateTicketStatusAccepted(long TicketId);
    Ticket updateTicketStatusSentToAdmin(long TicketId);
    Ticket updateTicketStatusPending(long TicketId);
    String deleteTicket(Long id);
   List<Ticket> getFrequentlyMaintainedItem(ItemGroupEnum itemGroup, String year);

    Ticket updateTicketStatusRejected(long ticketId);
    Ticket updateTicketStatusInprogress(long ticketId);
    List<Ticket> getTicketsByItemId(long itemId);

    Ticket updateTicketStatusCompleted(long ticketId);
}



