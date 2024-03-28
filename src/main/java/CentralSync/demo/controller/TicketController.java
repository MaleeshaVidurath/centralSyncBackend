package CentralSync.demo.controller;

import CentralSync.demo.model.Ticket;
import CentralSync.demo.exception.TicketNotFoundException;
import CentralSync.demo.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController

public class TicketController {
    @Autowired
    private TicketRepository ticketRepository;
    @PostMapping("/ticket")
    Ticket newTicket(@RequestBody Ticket newTicket){
        return ticketRepository.save(newTicket);

    }

    @GetMapping("/tickets")
    List<Ticket> getAllTickets(){return ticketRepository.findAll();
    }
    @GetMapping("ticket/{id}")
    Ticket getTicketById(@PathVariable Long id){
        return ticketRepository.findById(id)
                .orElseThrow(()->new TicketNotFoundException(id));
    }

    @PutMapping("/ticket/{id}")
    public Ticket updateTicket(@RequestBody Ticket newTicket, @PathVariable Long id) {
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

    @DeleteMapping("/ticket/{id}")
    String deleteTicket(@PathVariable Long id){
        if(!ticketRepository.existsById(id)){
            throw new TicketNotFoundException(id);
        }
        ticketRepository.deleteById(id);
        return "Ticket with id "+id+" has been deleted success";
    }
}
