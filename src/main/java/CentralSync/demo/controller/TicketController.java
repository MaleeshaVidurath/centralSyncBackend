package CentralSync.demo.controller;


import CentralSync.demo.model.Ticket;
import CentralSync.demo.exception.TicketNotFoundException;
import CentralSync.demo.model.User;
import CentralSync.demo.repository.InventoryItemRepository;
import CentralSync.demo.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ticket")
@CrossOrigin
public class TicketController {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid Ticket ticket, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }
        ticketService.saveTicket(ticket);
        return ResponseEntity.ok("New ticket is added");

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
