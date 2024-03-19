package CentralSync.demo.service;

import CentralSync.demo.model.Ticket;
import CentralSync.demo.exception.TicketNotFoundException;
import CentralSync.demo.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImplementation implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        return ticketRepository.findById(id);
    }


    @Override
    public Ticket updateTicket(Long id, Ticket newTicket) {
        return ticketRepository.findById(id)
                .map(ticket -> {
                    ticket.setTopic(newTicket.getTopic());
                    ticket.setDescription(newTicket.getDescription());
                    ticket.setStatus(newTicket.getStatus());
                    ticket.setDate(newTicket.getDate());
                    return ticketRepository.save(ticket);
                })
                .orElseThrow(() -> new TicketNotFoundException(id));
    }


    @Override
    public String deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new TicketNotFoundException(id);
        }
        ticketRepository.deleteById(id);
        return "Ticket with id " + id + " has been deleted successfully";
    }
}



