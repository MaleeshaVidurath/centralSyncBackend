package CentralSync.demo.controller;


import CentralSync.demo.model.Ticket;
import CentralSync.demo.exception.TicketNotFoundException;
import CentralSync.demo.model.TicketStatus;
import CentralSync.demo.model.UserStatus;
import CentralSync.demo.service.UserActivityLogService;
import CentralSync.demo.model.User;
import CentralSync.demo.repository.InventoryItemRepository;
import CentralSync.demo.service.TicketService;
import CentralSync.demo.service.UserActivityLogService;
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

@CrossOrigin(origins = "http://localhost:3000")
public class TicketController {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private UserActivityLogService userActivityLogService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid Ticket ticket, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }
        ticket.setTicketStatus(TicketStatus.PENDING);
        Ticket savedticket=ticketService.saveTicket(ticket);
        // Log user activity
        userActivityLogService.logUserActivity(savedticket.getTicketId(), "Ticket added");

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

    @PatchMapping("/updateStatusreviewed/{TicketId}")
    public ResponseEntity<?> updateTicketStatusReviewed(@PathVariable long TicketId) {

        Ticket status=ticketService.updateTicketStatusReviewed(TicketId);
        // Log the user activity for the update
        userActivityLogService.logUserActivity(status.getTicketId(), "Maintenece ticket Reviewed");
        return ResponseEntity.ok(" Ticket status is updated");

    }

    @PatchMapping("/updateStatussendtoadmin/{TicketId}")
    public ResponseEntity<?> updateTicketStatusSENDTOADMIN(@PathVariable long TicketId) {

        Ticket status=ticketService.updateTicketStatusSENDTOADMIN(TicketId);
        // Log the user activity for the update
        userActivityLogService.logUserActivity(status.getTicketId(), "Maintenece ticket Send to Admin");
        return ResponseEntity.ok(" Ticket status is updated");

    }


    @DeleteMapping("/delete/{id}")
    String deleteTicket(@PathVariable Long id){
        return ticketService.deleteTicket(id);
    }
}
