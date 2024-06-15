package CentralSync.demo.controller;

import CentralSync.demo.dto.InventoryRequestDTO;
import CentralSync.demo.model.InventoryRequest;
import CentralSync.demo.model.User;
import CentralSync.demo.service.EmailSenderService;
import CentralSync.demo.service.InventoryRequestService;
import CentralSync.demo.service.LoginService;
import CentralSync.demo.service.UserActivityLogService;
import CentralSync.demo.service.UserServiceImplementation;
import CentralSync.demo.util.InventoryRequestConverter;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/request")
@CrossOrigin
public class InventoryRequestController {

    private static final Logger logger = LoggerFactory.getLogger(InventoryRequestController.class);

    @Autowired
    private InventoryRequestService requestService;

    @Autowired
    private EmailSenderService emailSenderService;


    @Autowired
    private UserActivityLogService userActivityLogService;

    @Autowired

    private UserServiceImplementation userService;

    private final InventoryRequestService inventoryRequestService;

    @Autowired
    public InventoryRequestController(InventoryRequestService inventoryRequestService) {
        this.inventoryRequestService = inventoryRequestService;
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<InventoryRequest>> getRequestsByUserId(@PathVariable Long userId) {
        List<InventoryRequest> requests = requestService.getRequestsByUserId(userId);
        if (requests.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(requests);
    }
    @GetMapping("/user/details/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = requestService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }


    @PostMapping("/add")
    public ResponseEntity<?> addUserRequest(
            @Valid @ModelAttribute InventoryRequestDTO inventoryRequestDTO,
            BindingResult bindingResult) {

        logger.info("Received add request with details: {}", inventoryRequestDTO);

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            logger.error("Validation errors: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }


        MultipartFile file = inventoryRequestDTO.getFile();
        String filePath = "";
        String fileDownloadUri = null;

        if (file != null && !file.isEmpty()) {
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
                filePath = path.toString();

                // Construct the file download URI
                fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/uploads/")
                        .path(file.getOriginalFilename())
                        .toUriString();

            } catch (IOException e) {
                logger.error("Failed to save file", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to save file: " + e.getMessage());
            }
        }

        try {
            // Fetch user object
            User user = userService.getUserById(inventoryRequestDTO.getUserId());

            // Use InventoryRequestConverter to convert DTO to entity
            InventoryRequest inventoryRequest = InventoryRequestConverter.toEntity(inventoryRequestDTO, user);
            inventoryRequest.setFilePath(filePath);

            // Save the request
            InventoryRequest savedRequest = requestService.saveRequest(inventoryRequest);

            // Log user activity
            Long actorId=loginService.userId;
            userActivityLogService.logUserActivity(actorId,savedRequest.getReqId(), "New Inventory request added");

            logger.info("New Inventory request added: {}", savedRequest);
            return ResponseEntity.ok(Map.of(
                    "message", "New Inventory request is added",
                    "request", savedRequest,
                    "fileDownloadUri", fileDownloadUri != null ? fileDownloadUri : "No attachment"
            ));
        } catch (Exception e) {
            logger.error("An unexpected error occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

//    @GetMapping
//    public ResponseEntity<List<InventoryRequest>> getAllInventoryRequests() {
//        List<InventoryRequest> requests = requestService.getAllRequests();
//        return ResponseEntity.ok(requests);
//    }

    @GetMapping("/getById/{reqId}")
    public ResponseEntity<?> listById(@PathVariable long reqId) {
        InventoryRequest request = requestService.getRequestById(reqId);
        if (request != null) {
            return ResponseEntity.ok(request);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateById/{requestId}")
    public ResponseEntity<?> updateRequest(@RequestBody InventoryRequest newRequest, @PathVariable long requestId) {
        InventoryRequest updatedRequest = requestService.updateRequestById(newRequest, requestId);
        if (updatedRequest != null) {
            Long actorId=loginService.userId;
            userActivityLogService.logUserActivity(actorId,updatedRequest.getReqId(), "Inventory request updated");
            return ResponseEntity.ok(updatedRequest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/updateStatus/accept/{reqId}")
    public ResponseEntity<?> updateStatusAccept(@PathVariable long reqId) {
        InventoryRequest updatedRequest = requestService.updateInReqStatusAccept(reqId);
        if (updatedRequest != null) {
            Long actorId=loginService.userId;
            userActivityLogService.logUserActivity(actorId,updatedRequest.getReqId(), "Inventory request approved");
            return ResponseEntity.ok(updatedRequest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/updateStatus/reject/{reqId}")
    public ResponseEntity<?> updateStatusReject(@PathVariable long reqId) {
        InventoryRequest updatedRequest = requestService.updateInReqStatusReject(reqId);
        if (updatedRequest != null) {
            Long actorId=loginService.userId;
            userActivityLogService.logUserActivity(actorId,updatedRequest.getReqId(), "Inventory request rejected");
            return ResponseEntity.ok(updatedRequest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/updateStatus/sendToAdmin/{reqId}")
    public ResponseEntity<?> updateStatusSendToAdmin(@PathVariable long reqId) {
        InventoryRequest updatedRequest = requestService.updateInReqStatusSendToAdmin(reqId);
        if (updatedRequest != null) {
            Long actorId=loginService.userId;
            userActivityLogService.logUserActivity(actorId,updatedRequest.getReqId(), "Inventory request sent to admin");
            return ResponseEntity.ok(updatedRequest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

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
