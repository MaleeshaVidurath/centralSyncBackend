package CentralSync.demo.service;

import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.Ticket;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TicketService {
    public Ticket saveTicket(Ticket ticket);
    public List<Ticket> getAllTickets();

    Optional<Ticket> findById(Long id);
    public Ticket updateTicket(Long id, Ticket newTicket);

    Ticket updateTicketStatusReviewed(long TicketId);

    Ticket updateTicketStatusSENDTOADMIN(long TicketId);

    Ticket updateTicketStatusPENDING(long TicketId);

    String deleteTicket(Long id);

   List<Ticket> getFrequentlyMaintainedItem(ItemGroupEnum itemGroup, String year);
    List<Ticket> getTicketsByItemId(long itemId);


}



