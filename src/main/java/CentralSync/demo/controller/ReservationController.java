package CentralSync.demo.controller;

import CentralSync.demo.exception.UserNotFoundException;
import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.Reservation;
import CentralSync.demo.model.Status;
import CentralSync.demo.model.User;
import CentralSync.demo.repository.ReservationRepository;
import CentralSync.demo.service.*;
import CentralSync.demo.util.FileUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservation")
@CrossOrigin("http://localhost:3000")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private UserActivityLogService userActivityLogService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;
    @Autowired
    private InventoryItemService inventoryItemService;



    @PostMapping("/add")
    public ResponseEntity<?> createReservation(@RequestPart("reservation") @Valid Reservation reservation,
                                              BindingResult bindingResult,
                                              @RequestPart(value = "file", required = false) MultipartFile file) {
        System.out.println(reservation);
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }

        InventoryItem inventoryItem = inventoryItemService.getItemById(reservation.getItemId());
        if (inventoryItem == null) {
            return new ResponseEntity<>("Item not found", HttpStatus.NOT_FOUND);
        }
        if (!inventoryItemService.isActive(reservation.getItemId())) {
            return new ResponseEntity<>("Inventory item is inactive and cannot be used", HttpStatus.FORBIDDEN);
        }

        if (file != null && !file.isEmpty()) {
            try {
                String filePath = FileUtil.saveFile(file, file.getOriginalFilename());
                reservation.setFilePath(filePath);

            } catch (IOException e) {

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
            }
        }
        reservation.setStatus(Status.PENDING);
        Reservation savedReservation = reservationService.saveReservation(reservation);
        if (savedReservation.getResId() != null) {
            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId, savedReservation.getResId(), "New reservation added");
            return new ResponseEntity<>(savedReservation, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Reservation ID is null after saving", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getById/{resId}")
    public ResponseEntity<?> getReservationById(@PathVariable Long resId) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(resId);
        if (reservationOptional.isPresent()) {
            Reservation reservation = reservationOptional.get();
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Reservation not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getFileById/{resId}")
    public ResponseEntity<UrlResource> downloadFile(@PathVariable Long resId) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(resId);
        if (reservationOptional.isPresent()) {
            Reservation reservation = reservationOptional.get();
            String filePath = reservation.getFilePath();
            Path path = Paths.get(filePath);
            try {
                UrlResource resource = new UrlResource(path.toUri());
                if (Files.exists(path) && Files.isReadable(path)) {
                    return ResponseEntity.ok()
                            .header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
                            .body(resource);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAll")
    public List<Reservation> getAllReservation(){
        return reservationService.getAllReservation();
    }

    @GetMapping("/getAllById/{userId}")
    public List<Reservation> getReservationByUserId(@PathVariable Long userId) {
        return reservationService.getReservationByUserId(userId);
    }

    // PUT mapping for updating an existing reservation
    @PutMapping("/updateById/{resId}")
    public ResponseEntity<?> updateReservation(@PathVariable Long resId,
                                              @RequestParam("reason") String reason,
                                              @RequestParam("startDate") String startDate,
                                              @RequestParam("endDate") String endDate,
                                              @RequestParam("reservationQuantity") int reservationQuantity,
                                              @RequestParam(value = "file", required = false) MultipartFile file) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localStartDate = LocalDate.parse(startDate, formatter);
        LocalDate localEndDate = LocalDate.parse(endDate, formatter);

        try {
            // Retrieve the existing reservation by its ID
            Reservation existingReservation = reservationService.getReservationById(resId);

            if (existingReservation.getStatus() != Status.PENDING) {
                return new ResponseEntity<>("Only reservations with PENDING status can be updated.", HttpStatus.BAD_REQUEST);
            }

            // Update the reservation properties
            existingReservation.setReason(reason);
            existingReservation.setStartDate(localStartDate);
            existingReservation.setEndDate(localEndDate);
            existingReservation.setReservationQuantity(reservationQuantity);

            // Check if a new file is uploaded
            if (file != null && !file.isEmpty()) {
                String uploadFolder = "uploads/";
                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadFolder + file.getOriginalFilename());
                Files.write(path, bytes);

                // Set the file path for the existing reservation
                existingReservation.setFilePath(path.toString());
            }

            // Save the updated reservation to the database
            Reservation updatedReservation = reservationService.saveReservation(existingReservation);
            // Log user activity
            Long actorId=loginService.userId;
            userActivityLogService.logUserActivity(actorId,updatedReservation.getResId(), "Reservation updated");

            String adminEmail = loginService.getEmailByRole("ADMIN");
            if (adminEmail == null) {
                throw new RuntimeException("Admin email not found");
            }

            String subject = "Reservation Updated : " + updatedReservation.getResId();
            String text = "The following reservation has been updated:\n\n" +
                    "Reservation ID: " + updatedReservation.getResId() + "\n" +
                    "Reason: " + updatedReservation.getReason() + "\n" +
                    "End Date: " + updatedReservation.getEndDate() + "\n" +
                    "Reservation Quantity: " + updatedReservation.getReservationQuantity() + "\n" +
                    "start Date: " + updatedReservation.getStartDate() + "\n" +
                    "Status: " + updatedReservation.getStatus();

            emailSenderService.sendSimpleEmail(adminEmail, subject, text);

            return new ResponseEntity<>(updatedReservation, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update reservation.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteById/{resId}")
    public String deleteReservation(@PathVariable Long resId){
        return reservationService.deleteReservationById(resId);
    }

    @PatchMapping("/updateStatus/accept/{resId}")
    public ResponseEntity<?> updateStatusAccept(@PathVariable Long resId, @RequestBody Map<String, String> requestBody) {
        String note = requestBody.get("note");
        try {
            Reservation updatedReservation = reservationService.updateResStatusAccept(resId);

            Long userId = updatedReservation.getUserId();

            User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
            String userEmail = user.getEmail();

            if (note != null && !note.trim().isEmpty()) {
                String subject = "Reservation Approved";
                String body = "The following Reservation has been Approved:\n\n" +
                        "Reservation ID: " + resId + "\n" +
                        "Reason: " + updatedReservation.getReason() + "\n" +
                        "Reservation Quantity: " + updatedReservation.getReservationQuantity() + "\n" +
                        "Start Date: " + updatedReservation.getStartDate() + "\n\n" +
                        "End Date: " + updatedReservation.getEndDate() + "\n\n" +
                        "Note: " + note + "\n\n" +
                        "Computer Generated Email By CENTRAL SYNC ®";

                emailSenderService.sendSimpleEmail(userEmail, subject, body);
            }

            // Log user activity
            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId, updatedReservation.getResId(), "Reservation accepted");

            return new ResponseEntity<>(updatedReservation, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update reservation status.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PatchMapping("/updateStatus/reject/{resId}")
    public ResponseEntity<?> updateStatusReject(@PathVariable Long resId,@RequestBody Map<String, String> requestBody) {
        String note = requestBody.get("note");
        try {
            Reservation status= reservationService.updateResStatusReject( resId);

            Long userId = status.getUserId();

            User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
            String userEmail = user.getEmail();

            if (note != null && !note.trim().isEmpty()){
                String subject = "Reservation Rejected";
                String body = "The following Reservation has been Rejected:\n\n" +
                        "Reservation ID: " + resId + "\n" +
                        "Reason: " + status.getReason() + "\n" +
                        "Reservation Quantity: " + status.getReservationQuantity() + "\n" +
                        "Start Date: " + status.getStartDate() + "\n\n" +
                        "End Date: " + status.getEndDate() + "\n\n" +
                        "Note: " + note + "\n\n"+
                        "Computer Generated Email By CENTRAL SYNC ®" ;

                emailSenderService.sendSimpleEmail(userEmail, subject, body);
            }
            // Log user activity
            Long actorId=loginService.userId;
            userActivityLogService.logUserActivity(actorId,status.getResId(), "Reservation rejected");
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update reservation status.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/pending/count")
    public long getPendingReservationCount() {
        return reservationRepository.countPendingReservation();
    }

    @GetMapping("/pendingByUserId/count")
    public ResponseEntity<Long> getPendingCountByUserId() {
        try {
            Long userId=loginService.userId;
            Long pendingCount = reservationService.countByStatusAndUserId(Status.PENDING, userId);
            return new ResponseEntity<>(pendingCount, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}