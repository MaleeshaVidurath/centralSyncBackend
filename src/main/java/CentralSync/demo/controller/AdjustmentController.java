package CentralSync.demo.controller;

import CentralSync.demo.model.Adjustment;
import CentralSync.demo.model.Status;
import CentralSync.demo.repository.AdjustmentRepository;
import CentralSync.demo.service.AdjustmentService;
import CentralSync.demo.service.EmailSenderService;
import CentralSync.demo.service.UserActivityLogService;
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
    private UserActivityLogService userActivityLogService;
    @Autowired
    private EmailSenderService emailSenderService;

    @PostMapping("/add")
    public ResponseEntity<?> createAdjustment(@RequestParam("reason") String reason,
                                              @RequestParam("description") String description,
                                              @RequestParam("adjustedQuantity") int adjustedQuantity,
                                              @RequestParam("date") String date,
                                              @RequestParam("itemId") long itemId,
                                              @RequestParam("file") MultipartFile file) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        try {
            // Save the file to a designated folder
            String uploadFolder = "uploads/";
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadFolder + file.getOriginalFilename());
            Files.write(path, bytes);

            // Create a new Adjustment object and set its properties
            Adjustment adjustment = new Adjustment();
            adjustment.setItemId(itemId);
            adjustment.setDescription(description);
            adjustment.setReason(reason);
            adjustment.setAdjustedQuantity(adjustedQuantity);
            adjustment.setDate(localDate);
            adjustment.setFilePath(path.toString());
            adjustment.setStatus(Status.PENDING);

            // Save the Adjustment object to the database
            Adjustment savedAdjustment = adjustmentService.saveAdjustment(adjustment);
            // Log user activity
            userActivityLogService.logUserActivity(savedAdjustment.getAdjId(), "New adjustment added");
            return new ResponseEntity<>(savedAdjustment, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to upload file.", HttpStatus.INTERNAL_SERVER_ERROR);
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
            userActivityLogService.logUserActivity(updatedAdjustment.getAdjId(), "Adjustment Updated");

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
    public ResponseEntity<?> updateStatusAccept(@PathVariable Long adjId,@RequestBody Map<String, String> requestBody){

        String note = requestBody.get("note");
        try {
            Adjustment updatedAdjustment = adjustmentService.updateAdjStatusAccept(adjId);

            if (note != null && !note.trim().isEmpty()){
                // Sending email
                String toEmail = "dileepaashen81@gmail.com";
                String subject = "Adjustment Approved";
                String body = "The following Adjustment has been Approved:\n\n" +
                        "Adjustment ID: " + adjId + "\n" +
                        "Reason: " + updatedAdjustment.getReason() + "\n" +
                        "Adjusted Quantity: " + updatedAdjustment.getAdjustedQuantity() + "\n" +
                        "Description: " + updatedAdjustment.getDescription() + "\n\n" +
                        "Note: " + note + "\n\n"+
                        "Computer Generated Email By CENTRAL SYNC ®" ;

                emailSenderService.sendSimpleEmail(toEmail, subject, body);
            }
            // Log user activity
            userActivityLogService.logUserActivity(updatedAdjustment.getAdjId(), "Adjustment accepted");
            return new ResponseEntity<>(updatedAdjustment, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update adjustment status.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/updateStatus/reject/{adjId}")
    public Adjustment updateStatusReject(@PathVariable Long adjId) {
        Adjustment status= adjustmentService.updateAdjStatusReject( adjId);
        // Log user activity
        userActivityLogService.logUserActivity(status.getAdjId(), "Adjustment rejected");
        return (status);
    }
}