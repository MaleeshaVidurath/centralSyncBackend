package CentralSync.demo.controller;


import CentralSync.demo.Model.Ticket;
import CentralSync.demo.exception.TicketNotFoundException;
import CentralSync.demo.Services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/ticket")
@CrossOrigin
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping("/add")
    public String add(@RequestBody Ticket ticket) {
        ticketService.saveTicket(ticket);
        return "New ticket is added";
    }

    @GetMapping("/getAll")
    public List<Ticket> list() {
        return ticketService.getAllTickets();
    }

    @GetMapping("tickets/{id}")
    Ticket findById(@PathVariable Long id) {
        return ticketService.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
    }

    @PutMapping("/update/{id}")
    public Ticket updateTicketById(@RequestBody Ticket newTicket, @PathVariable Long id) {
        return ticketService.updateTicket(id, newTicket);
    }

    @DeleteMapping("/delete/{id}")
    String deleteTicket(@PathVariable Long id){
        return ticketService.deleteTicket(id);
    }
}
