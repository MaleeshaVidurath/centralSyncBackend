package CentralSync.demo.controller;

import CentralSync.demo.model.InventoryRequest;
import CentralSync.demo.service.EmailSenderService;
import CentralSync.demo.service.InventoryRequestService;
import CentralSync.demo.service.UserActivityLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/request")
@CrossOrigin(origins = "http://localhost:8080")
public class InventoryRequestController {

    private static final Logger logger = LoggerFactory.getLogger(InventoryRequestController.class);

    @Autowired
    private InventoryRequestService requestService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private UserActivityLogService userActivityLogService;

  /*  // Add new inventory request API
    @PostMapping("/add")
    public ResponseEntity<?> addUserRequest(
            @Valid @ModelAttribute InventoryRequest inventoryRequest,
            BindingResult bindingResult,
            @RequestParam("file") MultipartFile file) {

        logger.info("Received add request with details: {}", inventoryRequest);

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
            logger.error("Validation errors: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        if (file.isEmpty()) {
            logger.error("File must not be empty");
            return ResponseEntity.badRequest().body("File must not be empty");
        }

        try {
            // Save the file to a designated folder
            String uploadFolder = "uploads/";
            Path uploadPath = Paths.get(uploadFolder);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadFolder + file.getOriginalFilename());
            Files.write(path, bytes);

            logger.info("File saved at path: {}", path.toString());

            // Set the file path to the inventory request
            inventoryRequest.setFilePath(path.toString());

            // Save the request
            InventoryRequest savedRequest = requestService.saveRequest(inventoryRequest);

            // Log user activity
            userActivityLogService.logUserActivity(savedRequest.getReqId(), "New Inventory request added");

            // Optionally, return the URI of the uploaded file
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/")
                    .path(file.getOriginalFilename())
                    .toUriString();

            logger.info("New Inventory request added: {}", savedRequest);
            return ResponseEntity.ok(Map.of(
                    "message", "New Inventory request is added",
                    "request", savedRequest,
                    "fileDownloadUri", fileDownloadUri
            ));
        } catch (IOException e) {
            logger.error("Failed to save file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save file: " + e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }
*/
    // Fetch inventory request by ID
    @GetMapping("/getById/{reqId}")
    public ResponseEntity<?> listById(@PathVariable long reqId) {
        InventoryRequest request = requestService.getRequestById(reqId);
        if (request != null) {
            return ResponseEntity.ok(request);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update inventory request by ID
    @PutMapping("/updateById/{requestId}")
    public ResponseEntity<?> updateRequest(@RequestBody InventoryRequest newRequest, @PathVariable long requestId) {
        InventoryRequest updatedRequest = requestService.updateRequestById(newRequest, requestId);
        if (updatedRequest != null) {
            userActivityLogService.logUserActivity(updatedRequest.getReqId(), "Inventory request updated");
            return ResponseEntity.ok(updatedRequest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Accept inventory request status
    @PatchMapping("/updateStatus/accept/{reqId}")
    public ResponseEntity<?> updateStatusAccept(@PathVariable long reqId) {
        InventoryRequest updatedRequest = requestService.updateInReqStatusAccept(reqId);
        if (updatedRequest != null) {
            return ResponseEntity.ok(updatedRequest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Reject inventory request status
    @PatchMapping("/updateStatus/reject/{reqId}")
    public ResponseEntity<?> updateStatusReject(@PathVariable long reqId) {
        InventoryRequest updatedRequest = requestService.updateInReqStatusReject(reqId);
        if (updatedRequest != null) {
            return ResponseEntity.ok(updatedRequest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Send inventory request status to admin
    @PatchMapping("/updateStatus/sendToAdmin/{reqId}")
    public ResponseEntity<?> updateStatusSendToAdmin(@PathVariable long reqId) {
        InventoryRequest updatedRequest = requestService.updateInReqStatusSendToAdmin(reqId);
        if (updatedRequest != null) {
            return ResponseEntity.ok(updatedRequest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete inventory request by ID
    @DeleteMapping("/deleteRequest/{requestId}")
    public ResponseEntity<String> deleteRequest(@PathVariable long requestId) {
        String result = requestService.deleteRequestById(requestId);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
