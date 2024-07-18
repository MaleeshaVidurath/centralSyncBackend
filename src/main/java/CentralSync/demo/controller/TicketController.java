package CentralSync.demo.controller;

import CentralSync.demo.exception.TicketNotFoundException;
import CentralSync.demo.model.*;
import CentralSync.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
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
    @Autowired
    private WSService wsService;
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Validated(CreateGroup.class)  Ticket ticket, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }

            ticket.setTicketStatus(TicketStatus.PENDING);
            Ticket savedticket = ticketService.saveTicket(ticket);
            // Log user activity
            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId, savedticket.getTicketId(), "New issue ticket added");

        // Find user IDs with the role "REQUEST_HANDLER"
        List<Long> requestHandlerIds = userService.findUserIdsByRole("REQUEST_HANDLER");

        // Send notifications to all "REQUEST_HANDLER" users
        String message = "New ticket has been added to ticket section";
        requestHandlerIds.forEach(userId -> wsService.notifyUser(String.valueOf(userId), message));

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
    public ResponseEntity<?> updateTicketById(@RequestBody @Validated(UpdateGroup.class) Ticket newTicket, BindingResult bindingResult,
                                              @PathVariable Long id) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }
        Ticket updatedTicket = ticketService.updateTicket(id, newTicket);
        return ResponseEntity.ok(updatedTicket);
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
            userActivityLogService.logUserActivity(actorId, updatedTicket.getTicketId(), "Issue ticket accepted");

            // Send notification to the user
            Long userId = updatedTicket.getUser().getUserId();
            String message = "Your ticket has been accepted.We will start working on it soon.";
            wsService.notifyUser(String.valueOf(userId), message);

            return ResponseEntity.ok(" Ticket status is updated");
        }
        catch (Exception e) {
            return new ResponseEntity<>("Failed to update ticket status.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PatchMapping("/sendtoadmin/{TicketId}")
    public ResponseEntity<?> updateTicketStatusSendToAdmin(@PathVariable long TicketId, @RequestBody Map<String, String> requestBody) {
        String note = requestBody.get("note");
        try {

            Optional<Ticket> optionalTicket = ticketService.findById(TicketId);
            if (!optionalTicket.isPresent()) {
                return new ResponseEntity<>("Ticket not found.", HttpStatus.NOT_FOUND);
            }

            Ticket status = ticketService.updateTicketStatusSentToAdmin(TicketId,note);

            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId,status.getTicketId(), "Issue ticket sent to admin");


            // Send notification to the user
            Long userId = status.getUser().getUserId();
            String message = "Your ticket has been sent to admin for further review.";
            wsService.notifyUser(String.valueOf(userId), message);

            return ResponseEntity.ok("Ticket status is updated");

        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update ticket status.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/adminreject/{TicketId}")
    public ResponseEntity<?> updateTicketStatusRejectedByAdmin(@PathVariable long TicketId, @RequestBody Map<String, String> requestBody) {
        String note = requestBody.get("note");
        try {
            Optional<Ticket> optionalTicket = ticketService.findById(TicketId);
            if (!optionalTicket.isPresent()) {
                return new ResponseEntity<>("Ticket not found.", HttpStatus.NOT_FOUND);
            }
            Ticket ticket = optionalTicket.get();
            Ticket status = ticketService.updateTicketStatusRejectedByAdmin(TicketId);

            // Send notification to the user
            Long userId = status.getUser().getUserId();
            String message = "Your ticket has rejected by admin.There is no issue with item.";
            wsService.notifyUser(String.valueOf(userId), message);

            if (note != null && !note.trim().isEmpty()) {
                User user = ticket.getUser();
                if (user != null) {
                    String toEmail = user.getEmail();
                    System.out.println(toEmail);
                    String subject = "Ticket Rejection Notification";
                    String body = "We regret to inform you that your ticket with the following details has been rejected:\n\n" +
                            "Ticket ID: " + TicketId + "\n" +
                            "Reason for rejection: " + note + "\n\n" +
                            "If you have any questions or need further assistance, please contact our support team.\n\n" +
                            "Thank you for your understanding.\n\n" +
                            "Best regards,\n" +
                            "CENTRAL SYNC\n\n"+
                            "Computer Generated Email By CENTRAL SYNC ®";

                    emailSenderService.sendSimpleEmail(toEmail, subject, body);
                }
            }
            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId,status.getTicketId(), "Issue ticket rejected");
            return ResponseEntity.ok("Ticket status is updated");

        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update ticket status.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/requesthandlerreject/{TicketId}")
    public ResponseEntity<?> updateTicketStatusRejectedByRequestHandler(@PathVariable long TicketId, @RequestBody Map<String, String> requestBody) {
        String note = requestBody.get("note");

        try {
            Optional<Ticket> optionalTicket = ticketService.findById(TicketId);
            if (!optionalTicket.isPresent()) {
                return new ResponseEntity<>("Ticket not found.", HttpStatus.NOT_FOUND);
            }
            Ticket ticket = optionalTicket.get();
            Ticket status = ticketService.updateTicketStatusRejectedByRequestHandler(TicketId);

            // Send notification to the user
            Long userId = status.getUser().getUserId();
            String message = "Your ticket has rejected.There is no issue with item.";
            wsService.notifyUser(String.valueOf(userId), message);

            if (note != null && !note.trim().isEmpty()) {
                User user = ticket.getUser();
                if (user != null) {
                    String toEmail = user.getEmail();
                    System.out.println(toEmail);
                    String subject = "Ticket Rejection Notification";
                    String body = "We regret to inform you that your ticket with the following details has been rejected:\n\n" +
                            "Ticket ID: " + TicketId + "\n" +
                            "Reason for rejection: " + note + "\n\n" +
                            "If you have any questions or need further assistance, please contact our support team.\n\n" +
                            "Thank you for your understanding.\n\n" +
                            "Best regards,\n" +
                            "CENTRAL SYNC\n\n"+
                            "Computer Generated Email By CENTRAL SYNC ®";

                    emailSenderService.sendSimpleEmail(toEmail, subject, body);
                }
            }
            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId,status.getTicketId(), "Issue ticket rejected");
            return ResponseEntity.ok("Ticket status is updated");

        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update ticket status.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PatchMapping("/inprogress/{TicketId}")
    public ResponseEntity<?> updateTicketStatusInprogress(@PathVariable long TicketId, @RequestBody Map<String, String> requestBody) {
        String note = requestBody.get("note");
        String completionDate = requestBody.get("completionDate");
        try {
            Optional<Ticket> optionalTicket = ticketService.findById(TicketId);
            if (!optionalTicket.isPresent()) {
                return new ResponseEntity<>("Ticket not found.", HttpStatus.NOT_FOUND);
            }
            Ticket ticket = optionalTicket.get();
            Ticket status = ticketService.updateTicketStatusInprogress(TicketId);

            // Send notification to the user
            Long userId = status.getUser().getUserId();
            String message = "Your ticket has in reviewing progress.";
            wsService.notifyUser(String.valueOf(userId), message);

                User user = ticket.getUser();
                if (user != null) {
                    String toEmail = user.getEmail();
                    System.out.println(toEmail);
                    String subject = "Ticket Progress Notification";
                    String body =  "Dear " + user.getFirstName() + ",\n\n" +
                            "We are pleased to inform you that progress has started on your ticket with the following details:\n\n" +
                            "Ticket ID: " + TicketId + "\n" +
                            "Note: " + note + "\n\n" +
                            "Expected Completion Date: " + completionDate + "\n\n" +
                            "We will keep you updated on the progress and notify you once the issue is resolved.\n\n" +
                            "Thank you for your patience.\n\n" +
                            "Best regards,\n" +
                            "CENTRAL SYNC\n\n"+
                            "Computer Generated Email By CENTRAL SYNC ®";

                    emailSenderService.sendSimpleEmail(toEmail, subject, body);
                }

            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId,status.getTicketId(), "Start resolving the issue ticket.");
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

            // Send notification to the user
            Long userId = status.getUser().getUserId();
            String message = "Your ticket has progress has been completed.";
            wsService.notifyUser(String.valueOf(userId), message);

                User user = ticket.getUser();
                if (user != null) {
                    String toEmail = user.getEmail();
                    System.out.println("email"+toEmail);
                    String subject = "Ticket Resolution Notification";
                    String body =  "Dear " + user.getFirstName() + ",\n\n" +
                            "We are pleased to inform you that your ticket with the following details has been resolved:\n\n" +
                            "Ticket ID: " + TicketId + "\n" +
                            "Resolution Details: " + note + "\n\n" +
                            "If you have any further questions or issues, please do not hesitate to contact our support team.\n\n" +
                            "Thank you for your patience and understanding.\n\n" +
                            "Best regards,\n" +
                            "CENTRAL SYNC\n\n"+
                            "Computer Generated Email By CENTRAL SYNC ®";

                    emailSenderService.sendSimpleEmail(toEmail, subject, body);
                }

            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId,status.getTicketId(), "Complete issue ticket resolution.");
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

    @GetMapping("/getByItemId/{itemId}")
    public List<Ticket> ticketsByItemId(@PathVariable long itemId){
        return ticketService.getTicketsByItemId(itemId);
    }
}
