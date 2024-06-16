package CentralSync.demo.controller;
import CentralSync.demo.dto.ReqRes;
import CentralSync.demo.model.*;
import CentralSync.demo.exception.TicketNotFoundException;
import CentralSync.demo.service.*;
import CentralSync.demo.service.UserActivityLogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/ticket")
@CrossOrigin(origins = "http://localhost:3000")
public class TicketController {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private InventoryItemService inventoryItemService;
    @Autowired
    private UserActivityLogService userActivityLogService;
    @Autowired
    LoginService loginService;
    @PostMapping("/add")
    public ResponseEntity<?> add(@Validated(CreateGroup.class) @RequestBody @Valid Ticket ticket, BindingResult bindingResult, Principal principal) {
        String email = principal.getName(); // Fetching the logged-in user's email
        ReqRes response = loginService.getMyInfo(email); // Fetching user information by email
        Long userId = response.getUserId();
        InventoryItem item = ticket.getItemId();
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        } else if (inventoryItemService.isActive(item.getItemId())) {
            ticket.setTicketStatus(TicketStatus.PENDING);
            Ticket savedticket = ticketService.saveTicket(ticket);
            // Log user activity
            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId, savedticket.getTicketId(), "New Maintenance ticket added");
            return ResponseEntity.ok("New ticket is added");
        }
        return new ResponseEntity<>("Inventory item is inactive and cannot be used", HttpStatus.FORBIDDEN);
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
    public Ticket updateTicketById(@Validated(CreateGroup.class) @RequestBody Ticket newTicket, @PathVariable Long id) {
        return ticketService.updateTicket(id, newTicket);
    }
    @PatchMapping("/review/{TicketId}")
    public ResponseEntity<?> updateTicketStatusReviewed(@PathVariable long TicketId) {

        Ticket status = ticketService.updateTicketStatusReviewed(TicketId);
        // Log the user activity for the update
        Long actorId = loginService.userId;
        userActivityLogService.logUserActivity(actorId, status.getTicketId(), "Maintenance ticket Reviewed");
        return ResponseEntity.ok(" Ticket status is updated");

    }
    @PatchMapping("/sendtoadmin/{TicketId}")
    public ResponseEntity<?> updateTicketStatusSENDTOADMIN(@PathVariable long TicketId) {

        Ticket status = ticketService.updateTicketStatusSENDTOADMIN(TicketId);
        // Log the user activity for the update
        Long actorId = loginService.userId;
        userActivityLogService.logUserActivity(actorId, status.getTicketId(), "Maintenance ticket sent to Admin");
        return ResponseEntity.ok(" Ticket status is updated");

    }
    @DeleteMapping("/delete/{id}")
    String deleteTicket(@PathVariable Long id) {
        return ticketService.deleteTicket(id);
    }
    @GetMapping("/item")
    public List<Ticket> mostMaintainedItem(@RequestParam ItemGroupEnum itemGroup, @RequestParam String year) {
        return ticketService.getFrequentlyMaintainedItem(itemGroup, year);
    }

    @GetMapping("/getByItemId/{itemId}")
    public List<Ticket> ticketsByItemId(@PathVariable long itemId){
        return ticketService.getTicketsByItemId(itemId);
    }
}
