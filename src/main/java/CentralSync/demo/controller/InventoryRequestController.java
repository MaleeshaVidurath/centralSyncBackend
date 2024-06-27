package CentralSync.demo.controller;

import CentralSync.demo.dto.InventoryRequestDTO;
import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.InventoryRequest;
import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.User;
import CentralSync.demo.repository.InventoryRequestRepository;
import CentralSync.demo.service.*;
import CentralSync.demo.util.InventoryRequestConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/request")
@CrossOrigin
public class InventoryRequestController {

    private static final Logger logger = LoggerFactory.getLogger(InventoryRequestController.class);
    private static final String UPLOAD_FOLDER = "uploads/";

    private final InventoryRequestService inventoryRequestService;
    private final EmailSenderService emailSenderService;
    private final UserActivityLogService userActivityLogService;
    private final UserServiceImplementation userService;
    private final LoginService loginService;
    private final InventoryItemServiceImpl inventoryItemServiceImpl;
    private final InventoryRequestConverter inventoryRequestConverter;
    private final InventoryItemService inventoryItemService;
    @Autowired
    private InventoryRequestRepository inventoryRequestRepository;

    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public InventoryRequestController(
            InventoryRequestService inventoryRequestService,
            EmailSenderService emailSenderService,
            UserActivityLogService userActivityLogService,
            UserServiceImplementation userService,
            LoginService loginService,
            InventoryItemServiceImpl inventoryItemServiceImpl,
            InventoryRequestConverter inventoryRequestConverter,
            InventoryItemService inventoryItemService) {
        this.inventoryRequestService = inventoryRequestService;
        this.emailSenderService = emailSenderService;
        this.userActivityLogService = userActivityLogService;
        this.userService = userService;
        this.loginService = loginService;
        this.inventoryItemServiceImpl = inventoryItemServiceImpl;
        this.inventoryRequestConverter = inventoryRequestConverter;
        this.inventoryItemService = inventoryItemService;
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
    public List<InventoryRequestDTO> list() {

        return inventoryRequestService.getAllRequests();

    }

    @GetMapping("/filtered")
    public ResponseEntity<?> filteredList(@RequestParam ItemGroupEnum itemGroup, @RequestParam String year) {
        if (itemGroup != null && year != null) {
            List<InventoryRequest> requests = inventoryRequestService.getRequestsByGroupAndYear(itemGroup, year);
            return ResponseEntity.status(HttpStatus.OK).body(requests);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/mostRequested")
    public ResponseEntity<?> mostRequestedItems(@RequestParam ItemGroupEnum itemGroup, @RequestParam String year) {
        if (itemGroup != null && year != null) {
            InventoryItem mostRequested = inventoryRequestService.getMostRequestedItem(itemGroup, year);
            return ResponseEntity.status(HttpStatus.OK).body(mostRequested);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/getById/{reqId}")
    public ResponseEntity<?> listById(@PathVariable long reqId) {
        InventoryRequest request = inventoryRequestService.getRequestById(reqId);
        if (request != null) {
            InventoryRequestDTO dto = inventoryRequestConverter.toDTO(request);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
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
        if (!inventoryItemService.isActive(inventoryRequestDTO.getItemId())) {
            return new ResponseEntity<>("Inventory item is inactive and cannot be used", HttpStatus.FORBIDDEN);
        }

        MultipartFile file = inventoryRequestDTO.getFile();
        String filePath = "";
        String fileDownloadUri = null;

        if (file != null && !file.isEmpty()) {
            try {
                Path uploadPath = Paths.get(UPLOAD_FOLDER);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                byte[] bytes = file.getBytes();
                Path path = uploadPath.resolve(file.getOriginalFilename());
                Files.write(path, bytes);

                logger.info("File saved at path: {}", path.toString());
                filePath = path.toString();

                fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/uploads/")
                        .path(file.getOriginalFilename())
                        .toUriString();

            } catch (IOException e) {
                logger.error("Failed to save file", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to save file: " + e.getMessage());
            }
        } else {
            logger.info("No file received in the request.");
        }

        try {
            User user = userService.getUserById(inventoryRequestDTO.getUserId());
            InventoryItem inventoryItem = inventoryItemServiceImpl.getItemById(inventoryRequestDTO.getItemId());

            logger.info("User fetched: {}", user);
            logger.info("Inventory item fetched: {}", inventoryItem);

            InventoryRequest inventoryRequest = inventoryRequestConverter.toEntity(inventoryRequestDTO, user, inventoryItem);
            logger.info("InventoryRequest created: {}", inventoryRequest);

            inventoryRequest.setFilePath(filePath);
            inventoryRequest.setUpdatedDateTime(LocalDateTime.now());

            InventoryRequest savedRequest = inventoryRequestService.saveRequest(inventoryRequest);

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


    @PutMapping("/updateById/{requestId}")
    public ResponseEntity<?> updateRequest(
            @Valid @ModelAttribute InventoryRequestDTO newRequestDTO,
            @PathVariable long requestId,
            BindingResult bindingResult) {

        logger.info("Received update request for ID {} with details: {}", requestId, newRequestDTO);

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            logger.error("Validation errors: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        InventoryRequest existingRequest = inventoryRequestService.getRequestById(requestId);
        if (existingRequest == null) {
            return ResponseEntity.notFound().build();
        }

        MultipartFile file = newRequestDTO.getFile();
        logger.info("File received: {}", file != null ? file.getOriginalFilename() : "No file");

        String filePath = existingRequest.getFilePath();
        String fileDownloadUri = null;

        if (file != null && !file.isEmpty()) {
            try {
                Path uploadPath = Paths.get(UPLOAD_FOLDER);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                    logger.info("Upload directory created: {}", uploadPath.toString());
                }
                byte[] bytes = file.getBytes();
                Path path = uploadPath.resolve(file.getOriginalFilename());
                Files.write(path, bytes);

                logger.info("File saved at path: {}", path.toString());
                filePath = path.toString();

                fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/uploads/")
                        .path(file.getOriginalFilename())
                        .toUriString();

                // Delete the old file if it exists
                if (existingRequest.getFilePath() != null) {
                    Path oldFilePath = Paths.get(existingRequest.getFilePath());
                    if (Files.exists(oldFilePath)) {
                        Files.delete(oldFilePath);
                        logger.info("Old file deleted at path: {}", oldFilePath.toString());
                    }
                }

            } catch (IOException e) {
                logger.error("Failed to save file", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to save file: " + e.getMessage());
            }
        } else {
            logger.info("No file received in the request.");
        }

        InventoryItem inventoryItem = inventoryItemServiceImpl.getItemById(newRequestDTO.getItemId());
        if (inventoryItem == null) {
            return ResponseEntity.badRequest().body("Invalid InventoryItem ID");
        }

        try {
            InventoryRequest updatedRequest = inventoryRequestService.updateRequestById(newRequestDTO, existingRequest, inventoryItem);
            updatedRequest.setFilePath(filePath);
            updatedRequest.setUpdatedDateTime(LocalDateTime.now());

            InventoryRequest savedRequest = inventoryRequestService.saveRequest(updatedRequest);

            logger.info("Inventory request updated: {}", savedRequest);
            return ResponseEntity.ok(Map.of(
                    "message", "Inventory request updated successfully",
                    "request", savedRequest,
                    "fileDownloadUri", fileDownloadUri != null ? fileDownloadUri : "No attachment"
            ));
        } catch (Exception e) {
            logger.error("An unexpected error occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }


    @PatchMapping("/updateStatus/dispatch/{reqId}")
    public ResponseEntity<?> updateStatusDispatch(@PathVariable long reqId, @RequestParam String email) {
        InventoryRequest updatedRequest = inventoryRequestService.updateInReqStatusDispatch(reqId, email);
        if (updatedRequest != null) {
            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId, updatedRequest.getReqId(), "Delivery request dispatched");

            // Send email with submission link
            String toEmail = email;
            String subject = "Item Delivery Confirmation";
            String body = "If you have successfully delivered the item, please click the link below to confirm the delivery.";
            String link = "http://localhost:8080/request/updateStatus/delivered/" + reqId;

            try {
                emailSenderService.sendMimeEmail(toEmail, subject, body, link);
            } catch (MessagingException | javax.mail.MessagingException e) {
                logger.error("Failed to send delivery confirmation email", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email: " + e.getMessage());
            }
            messagingTemplate.convertAndSend("/topic/notifications", "Dispatch update for request ID: " + reqId);
            return ResponseEntity.ok(updatedRequest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/updateStatus/reject/{reqId}")
    public ResponseEntity<?> updateStatusReject(@PathVariable long reqId) throws JsonProcessingException {
        InventoryRequest updatedRequest = inventoryRequestService.updateInReqStatusReject(reqId);
        if (updatedRequest != null) {
            System.out.println("Sending WebSocket notification for rejection");
            // Long actorId = loginService.userId;
            // userActivityLogService.logUserActivity(actorId, updatedRequest.getReqId(), "Inventory request rejected");
            // Create a JSON object to send as the notification message
            Map<String, String> notification = new HashMap<>();
            notification.put("type", "rejection");
            notification.put("message", "Rejection update for request ID: " + reqId);

            // Convert the map to a JSON string
            String notificationJson = new ObjectMapper().writeValueAsString(notification);
            messagingTemplate.convertAndSend("/topic/notifications", notificationJson);
            return ResponseEntity.ok(updatedRequest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/updateStatus/ItemReturned/{reqId}")
    public ResponseEntity<?> updateInReqStatusItemReturned(@PathVariable long reqId) {
        InventoryRequest updatedRequest = inventoryRequestService.updateInReqStatusItemReturned(reqId);
        if (updatedRequest != null) {
            Long actorId = loginService.userId;
            userActivityLogService.logUserActivity(actorId, updatedRequest.getReqId(), "Item returned");
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


    @GetMapping("/pending-all/count")
    public long getPendingRequestCount() {
        return inventoryRequestRepository.countPendingRequest();
    }

    @PostMapping("/sendSimpleEmail")
    public String sendSimpleEmail(@RequestParam String toEmail, @RequestParam String subject, @RequestParam String body) {
        emailSenderService.sendSimpleEmail(toEmail, subject, body);
        return "Simple email sent successfully";

    }

    @GetMapping("/getFileById/{reqId}")
    public ResponseEntity<UrlResource> downloadFile(@PathVariable Long reqId) {
        InventoryRequest request = inventoryRequestService.getRequestById(reqId);
        if (request != null) {
            String filePath = request.getFilePath();
            if (filePath != null && !filePath.isEmpty()) {
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
                    logger.error("Failed to download file", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
