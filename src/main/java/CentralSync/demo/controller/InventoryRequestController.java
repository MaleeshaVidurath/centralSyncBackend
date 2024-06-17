package CentralSync.demo.controller;

import CentralSync.demo.dto.InventoryRequestDTO;
import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.InventoryRequest;
import CentralSync.demo.model.User;
import CentralSync.demo.service.*;
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

    private final InventoryRequestService inventoryRequestService;
    private final EmailSenderService emailSenderService;
    private final UserActivityLogService userActivityLogService;
    private final UserServiceImplementation userService;
    private final LoginService loginService;
    private final InventoryItemServiceImpl inventoryItemServiceImpl;
    private final InventoryRequestConverter inventoryRequestConverter;

    @Autowired
    public InventoryRequestController(
            InventoryRequestService inventoryRequestService,
            EmailSenderService emailSenderService,
            UserActivityLogService userActivityLogService,
            UserServiceImplementation userService,
            LoginService loginService,
            InventoryItemServiceImpl inventoryItemServiceImpl,
            InventoryRequestConverter inventoryRequestConverter) {
        this.inventoryRequestService = inventoryRequestService;
        this.emailSenderService = emailSenderService;
        this.userActivityLogService = userActivityLogService;
        this.userService = userService;
        this.loginService = loginService;
        this.inventoryItemServiceImpl = inventoryItemServiceImpl;
        this.inventoryRequestConverter = inventoryRequestConverter;
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<InventoryRequest>> getRequestsByUserId(@PathVariable Long userId) {
        List<InventoryRequest> requests = inventoryRequestService.getRequestsByUserId(userId);
        if (requests.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/user/details/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = inventoryRequestService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
    @GetMapping("/getAll")
    public List<InventoryRequest> list() {
        return inventoryRequestService.getAllRequests();
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
            // Fetch user and item objects
            User user = userService.getUserById(inventoryRequestDTO.getUserId());
            InventoryItem inventoryItem = inventoryItemServiceImpl.getItemById(inventoryRequestDTO.getItemId());

            // Use InventoryRequestConverter to convert DTO to entity
            InventoryRequest inventoryRequest = inventoryRequestConverter.toEntity(inventoryRequestDTO, user, inventoryItem);
            inventoryRequest.setFilePath(filePath);

            // Save the request
            InventoryRequest savedRequest = inventoryRequestService.saveRequest(inventoryRequest);

            // Log user activity
           // Long actorId = loginService.userId;
           // userActivityLogService.logUserActivity(actorId, savedRequest.getReqId(), "New Inventory request added");

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

    @GetMapping("/getById/{reqId}")
    public ResponseEntity<?> listById(@PathVariable long reqId) {
        InventoryRequest request = inventoryRequestService.getRequestById(reqId);
        if (request != null) {
            return ResponseEntity.ok(request);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateById/{requestId}")
    public ResponseEntity<?> updateRequest(@RequestBody InventoryRequest newRequest, @PathVariable long requestId) {
        InventoryRequest updatedRequest = inventoryRequestService.updateRequestById(newRequest, requestId);
        if (updatedRequest != null) {
            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId, updatedRequest.getReqId(), "Inventory request updated");
            return ResponseEntity.ok(updatedRequest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/updateStatus/accept/{reqId}")
    public ResponseEntity<?> updateStatusAccept(@PathVariable long reqId) {
        InventoryRequest updatedRequest = inventoryRequestService.updateInReqStatusAccept(reqId);
        if (updatedRequest != null) {
            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId, updatedRequest.getReqId(), "Inventory request approved");
            return ResponseEntity.ok(updatedRequest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/updateStatus/reject/{reqId}")
    public ResponseEntity<?> updateStatusReject(@PathVariable long reqId) {
        InventoryRequest updatedRequest = inventoryRequestService.updateInReqStatusReject(reqId);
        if (updatedRequest != null) {
            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId, updatedRequest.getReqId(), "Inventory request rejected");
            return ResponseEntity.ok(updatedRequest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/updateStatus/sendToAdmin/{reqId}")
    public ResponseEntity<?> updateStatusSendToAdmin(@PathVariable long reqId) {
        InventoryRequest updatedRequest = inventoryRequestService.updateInReqStatusSendToAdmin(reqId);
        if (updatedRequest != null) {
            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId, updatedRequest.getReqId(), "Inventory request sent to admin");
            return ResponseEntity.ok(updatedRequest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteRequest/{requestId}")
    public ResponseEntity<String> deleteRequest(@PathVariable long requestId) {
        String result = inventoryRequestService.deleteRequestById(requestId);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
