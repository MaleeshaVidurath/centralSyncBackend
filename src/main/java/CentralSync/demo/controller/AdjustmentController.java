package CentralSync.demo.controller;

import CentralSync.demo.exception.UserNotFoundException;
import CentralSync.demo.model.*;
import CentralSync.demo.repository.AdjustmentRepository;
import CentralSync.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("/adjustment")
@CrossOrigin("http://localhost:3000")
public class AdjustmentController {

    @Autowired
    private AdjustmentService adjustmentService;
    @Autowired
    private AdjustmentRepository adjustmentRepository;
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
    public ResponseEntity<?> createAdjustment(@RequestParam("reason") String reason,
                                              @RequestParam("description") String description,
                                              @RequestParam("adjustedQuantity") int adjustedQuantity,
                                              @RequestParam("date") String date,
                                              @RequestParam("itemId") long itemId,
                                              @RequestParam("userId") long userId,
                                              @RequestParam(value = "file", required = false) MultipartFile file) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        Adjustment adjustment = new Adjustment();

        if (file != null && !file.isEmpty()) {
            try {
                String uploadFolder = "uploads/";
                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadFolder + file.getOriginalFilename());
                Files.write(path, bytes);
                adjustment.setFilePath(path.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>("Failed to upload file.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        adjustment.setItemId(itemId);
        adjustment.setUserId(userId);
        adjustment.setDescription(description);
        adjustment.setReason(reason);
        adjustment.setAdjustedQuantity(adjustedQuantity);
        adjustment.setDate(localDate);
        adjustment.setStatus(Status.PENDING);

        Adjustment savedAdjustment = adjustmentService.saveAdjustment(adjustment);

        if (savedAdjustment.getAdjId() != null) {
            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId, savedAdjustment.getAdjId(), "New adjustment added");
            return new ResponseEntity<>(savedAdjustment, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Adjustment ID is null after saving", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getById/{adjId}")
    public ResponseEntity<?> getAdjustmentById(@PathVariable Long adjId) {
        Optional<Adjustment> adjustmentOptional = adjustmentRepository.findById(adjId);
        if (adjustmentOptional.isPresent()) {
            Adjustment adjustment = adjustmentOptional.get();
            return new ResponseEntity<>(adjustment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Adjustment not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getFileById/{adjId}")
    public ResponseEntity<UrlResource> downloadFile(@PathVariable Long adjId) {
        Optional<Adjustment> adjustmentOptional = adjustmentRepository.findById(adjId);
        if (adjustmentOptional.isPresent()) {
            Adjustment adjustment = adjustmentOptional.get();
            String filePath = adjustment.getFilePath();
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
    public List<Adjustment> getAllAdjustments(){
        return adjustmentService.getAllAdjustments();
    }

    @GetMapping("/getAllById/{userId}")
    public List<Adjustment> getAdjustmentsByUserId(@PathVariable Long userId) {
        return adjustmentService.getAdjustmentsByUserId(userId);
    }

    // PUT mapping for updating an existing adjustment
    @PutMapping("/updateById/{adjId}")
    public ResponseEntity<?> updateAdjustment(@PathVariable Long adjId,
                                              @RequestParam("reason") String reason,
                                              @RequestParam("date") String date,
                                              @RequestParam("description") String description,
                                              @RequestParam("adjustedQuantity") int adjustedQuantity,
                                              @RequestParam(value = "file", required = false) MultipartFile file) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        try {
            // Retrieve the existing adjustment by its ID
            Adjustment existingAdjustment = adjustmentService.getAdjustmentById(adjId);

            // Update the adjustment properties
            existingAdjustment.setReason(reason);
            existingAdjustment.setDate(localDate);
            existingAdjustment.setDescription(description);
            existingAdjustment.setAdjustedQuantity(adjustedQuantity);

            // Check if a new file is uploaded
            if (file != null && !file.isEmpty()) {
                // Save the new file to a designated folder
                String uploadFolder = "uploads/";
                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadFolder + file.getOriginalFilename());
                Files.write(path, bytes);

                // Set the file path for the existing adjustment
                existingAdjustment.setFilePath(path.toString());
            }

            // Save the updated adjustment to the database
            Adjustment updatedAdjustment = adjustmentService.saveAdjustment(existingAdjustment);
            // Log user activity
            Long actorId=loginService.userId;
            userActivityLogService.logUserActivity(actorId,updatedAdjustment.getAdjId(), "Adjustment Updated");

            return new ResponseEntity<>(updatedAdjustment, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update adjustment.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteById/{adjId}")
    public String deleteAdjustment(@PathVariable Long adjId){
        return adjustmentService.deleteAdjustmentById(adjId);
    }

    @PatchMapping("/updateStatus/accept/{adjId}")
    public ResponseEntity<?> updateStatusAccept(@PathVariable Long adjId, @RequestBody Map<String, String> requestBody) {
        String note = requestBody.get("note");
        try {
            Adjustment updatedAdjustment = adjustmentService.updateAdjStatusAccept(adjId);

            Long userId = updatedAdjustment.getUserId();

            User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
            String userEmail = user.getEmail();

            if (note != null && !note.trim().isEmpty()) {
                String subject = "Adjustment Approved";
                String body = "The following Adjustment has been Approved:\n\n" +
                        "Adjustment ID: " + adjId + "\n" +
                        "Reason: " + updatedAdjustment.getReason() + "\n" +
                        "Adjusted Quantity: " + updatedAdjustment.getAdjustedQuantity() + "\n" +
                        "Description: " + updatedAdjustment.getDescription() + "\n\n" +
                        "Note: " + note + "\n\n" +
                        "Computer Generated Email By CENTRAL SYNC ®";

                emailSenderService.sendSimpleEmail(userEmail, subject, body);
            }

            // Log user activity
            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId, updatedAdjustment.getAdjId(), "Adjustment accepted");

            return new ResponseEntity<>(updatedAdjustment, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update adjustment status.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PatchMapping("/updateStatus/reject/{adjId}")
    public ResponseEntity<?> updateStatusReject(@PathVariable Long adjId,@RequestBody Map<String, String> requestBody) {
        String note = requestBody.get("note");
        try {
            Adjustment status= adjustmentService.updateAdjStatusReject( adjId);

            Long userId = status.getUserId();

            User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
            String userEmail = user.getEmail();

            if (note != null && !note.trim().isEmpty()){
                String subject = "Adjustment Rejected";
                String body = "The following Adjustment has been Rejected:\n\n" +
                        "Adjustment ID: " + adjId + "\n" +
                        "Reason: " + status.getReason() + "\n" +
                        "Adjusted Quantity: " + status.getAdjustedQuantity() + "\n" +
                        "Description: " + status.getDescription() + "\n\n" +
                        "Note: " + note + "\n\n"+
                        "Computer Generated Email By CENTRAL SYNC ®" ;

                emailSenderService.sendSimpleEmail(userEmail, subject, body);
            }
            // Log user activity
            Long actorId=loginService.userId;
            userActivityLogService.logUserActivity(actorId,status.getAdjId(), "Adjustment rejected");
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update adjustment status.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/pending/count")
    public long getPendingAdjustmentsCount() {
        return adjustmentRepository.countPendingAdjustments();
    }
}