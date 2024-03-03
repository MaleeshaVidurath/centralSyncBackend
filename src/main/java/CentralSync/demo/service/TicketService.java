package CentralSync.demo.service;

import CentralSync.demo.Model.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    public Ticket saveTicket(Ticket ticket);
    public List<Ticket> getAllTickets();

    Optional<Ticket> findById(Long id);
    public Ticket updateTicket(Long id, Ticket newTicket);
    String deleteTicket(Long id);
}
