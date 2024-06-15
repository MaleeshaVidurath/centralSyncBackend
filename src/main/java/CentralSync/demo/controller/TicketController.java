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
import java.util.Optional;
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
    EmailSenderService emailSenderService;
    @Autowired
    private InventoryItemService inventoryItemService;
    @Autowired
    private UserActivityLogService userActivityLogService;
    @Autowired
    LoginService loginService;
    @PostMapping("/add")
    public ResponseEntity<?> add(@Validated(CreateGroup.class) @RequestBody @Valid Ticket ticket, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }

            ticket.setTicketStatus(TicketStatus.PENDING);
            Ticket savedticket = ticketService.saveTicket(ticket);
            // Log user activity
            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId, savedticket.getTicketId(), "New Maintenance ticket added");
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

    @GetMapping("/{userId}")
    public List<Ticket> getUserTickets(@PathVariable Long userId){
        return ticketService.getTicketsByUser(userId);
    }
    @PutMapping("/update/{id}")
    public Ticket updateTicketById(@Validated(CreateGroup.class) @RequestBody Ticket newTicket, @PathVariable Long id) {
        return ticketService.updateTicket(id, newTicket);
    }
    @PatchMapping("/accept/{TicketId}")
    public ResponseEntity<?> updateTicketStatusAccepted(@PathVariable long TicketId,@RequestBody Map<String, String> requestBody) {

        try {
            Optional<Ticket> optionalTicket = ticketService.findById(TicketId);
            if (!optionalTicket.isPresent()) {
                return new ResponseEntity<>("Ticket not found.", HttpStatus.NOT_FOUND);
            }

            Ticket ticket = optionalTicket.get();
            Ticket updatedTicket = ticketService.updateTicketStatusAccepted(TicketId);


            // Log the user activity for the update
            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId, updatedTicket.getTicketId(), "Maintenance ticket Reviewed");
            return ResponseEntity.ok(" Ticket status is updated");
        }
        catch (Exception e) {
            return new ResponseEntity<>("Failed to update ticket status.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PatchMapping("/sendtoadmin/{TicketId}")
    public ResponseEntity<?> updateTicketStatusSendToAdmin(@PathVariable long TicketId, @RequestBody Map<String, String> requestBody) {
        try {
            Optional<Ticket> optionalTicket = ticketService.findById(TicketId);
            if (!optionalTicket.isPresent()) {
                return new ResponseEntity<>("Ticket not found.", HttpStatus.NOT_FOUND);
            }
            Ticket ticket = optionalTicket.get();
            Ticket status = ticketService.updateTicketStatusSentToAdmin(TicketId);

            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId,status.getTicketId(), "Maintenance ticket Reviewed");
            return ResponseEntity.ok("Ticket status is updated");

        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update ticket status.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/reject/{TicketId}")
    public ResponseEntity<?> updateTicketStatusRejected(@PathVariable long TicketId, @RequestBody Map<String, String> requestBody) {
        String note = requestBody.get("note");
        try {
            Optional<Ticket> optionalTicket = ticketService.findById(TicketId);
            if (!optionalTicket.isPresent()) {
                return new ResponseEntity<>("Ticket not found.", HttpStatus.NOT_FOUND);
            }
            Ticket ticket = optionalTicket.get();
            Ticket status = ticketService.updateTicketStatusRejected(TicketId);

            if (note != null && !note.trim().isEmpty()) {
                User user = ticket.getUser();
                if (user != null) {
                    String toEmail = user.getEmail();
                    System.out.println(toEmail);
                    String subject = "Ticket";
                    String body = " :\n\n" +
                            "Ticket ID: " + TicketId + "\n" +
                            "Note: " + note + "\n\n" +
                            "Computer Generated Email By CENTRAL SYNC ®";

                    emailSenderService.sendSimpleEmail(toEmail, subject, body);
                }
            }
            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId,status.getTicketId(), "Maintenance ticket Rejected");
            return ResponseEntity.ok("Ticket status is updated");

        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update ticket status.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/inprogress/{TicketId}")
    public ResponseEntity<?> updateTicketStatusInprogress(@PathVariable long TicketId, @RequestBody Map<String, String> requestBody) {
        String note = requestBody.get("note");
        try {
            Optional<Ticket> optionalTicket = ticketService.findById(TicketId);
            if (!optionalTicket.isPresent()) {
                return new ResponseEntity<>("Ticket not found.", HttpStatus.NOT_FOUND);
            }
            Ticket ticket = optionalTicket.get();
            Ticket status = ticketService.updateTicketStatusInprogress(TicketId);

            if (note != null && !note.trim().isEmpty()) {
                User user = ticket.getUser();
                if (user != null) {
                    String toEmail = user.getEmail();
                    System.out.println(toEmail);
                    String subject = "Ticket";
                    String body = " :\n\n" +
                            "Ticket ID: " + TicketId + "\n" +
                            "Note: " + note + "\n\n" +
                            "Computer Generated Email By CENTRAL SYNC ®";

                    emailSenderService.sendSimpleEmail(toEmail, subject, body);
                }
            }
            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId,status.getTicketId(), "Maintenance ticket Rejected");
            return ResponseEntity.ok("Ticket status is updated");

        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update ticket status.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/complete/{TicketId}")
    public ResponseEntity<?> updateTicketStatusComplete(@PathVariable long TicketId, @RequestBody Map<String, String> requestBody) {
        String note = requestBody.get("note");
        try {
            Optional<Ticket> optionalTicket = ticketService.findById(TicketId);
            if (!optionalTicket.isPresent()) {
                return new ResponseEntity<>("Ticket not found.", HttpStatus.NOT_FOUND);
            }
            Ticket ticket = optionalTicket.get();
            Ticket status = ticketService.updateTicketStatusCompleted(TicketId);

            if (note != null && !note.trim().isEmpty()) {
                User user = ticket.getUser();
                if (user != null) {
                    String toEmail = user.getEmail();
                    System.out.println(toEmail);
                    String subject = "Ticket";
                    String body = " :\n\n" +
                            "Ticket ID: " + TicketId + "\n" +
                            "Note: " + note + "\n\n" +
                            "Computer Generated Email By CENTRAL SYNC ®";

                    emailSenderService.sendSimpleEmail(toEmail, subject, body);
                }
            }
            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId,status.getTicketId(), "Maintenance ticket Rejected");
            return ResponseEntity.ok("Ticket status is updated");

        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update ticket status.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    String deleteTicket(@PathVariable Long id) {
        return ticketService.deleteTicket(id);
    }
    @GetMapping("/item")
    public List<Ticket> mostMaintainedItem(@RequestParam ItemGroupEnum itemGroup, @RequestParam String year) {
        return ticketService.getFrequentlyMaintainedItem(itemGroup, year);
    }
}
