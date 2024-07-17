package CentralSync.demo.controller;

import CentralSync.demo.exception.OrderingNotFoundException;
import CentralSync.demo.model.*;
import CentralSync.demo.service.ItemOrderService;
import CentralSync.demo.service.LoginService;
import CentralSync.demo.service.UserActivityLogService;
import CentralSync.demo.util.FileUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class ItemOrderController {
    private static final Logger logger = LoggerFactory.getLogger(InventoryItemController.class);
    private final ItemOrderService itemOrderService;
    private final UserActivityLogService userActivityLogService;
    private final LoginService loginService;

    public ItemOrderController(
            ItemOrderService itemOrderService,
            UserActivityLogService userActivityLogService,
            LoginService loginService) {
        this.itemOrderService = itemOrderService;
        this.userActivityLogService = userActivityLogService;
        this.loginService = loginService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestPart("order") @Valid ItemOrder itemOrder,
                                 BindingResult bindingResult,
                                 @RequestPart(value = "file", required = false) MultipartFile file) {

        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors for order: {}", itemOrder.getOrderId());
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }
        if (file != null && !file.isEmpty()) {
            try {
                String filePath = FileUtil.saveFile(file, file.getOriginalFilename());
                itemOrder.setFilePath(filePath);
                logger.info("File uploaded successfully for order: {}", itemOrder.getOrderId());
            } catch (IOException e) {
                logger.error("File upload failed for order: {}", itemOrder.getOrderId(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
            }
        }

        itemOrder.setStatus(OrderStatus.PENDING);
        ItemOrder savedItemOrder = itemOrderService.saveNewOrder(itemOrder);

        // Log user activity
        Long actorId = loginService.userId;
        userActivityLogService.logUserActivity(actorId, savedItemOrder.getOrderId(), "New order added");
        logger.info("Order added successfully: {}", itemOrder.getOrderId());


        return ResponseEntity.status(HttpStatus.CREATED).body("Order initiated");
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<InputStreamResource> downloadPdf(@PathVariable String fileName) throws IOException {
        String filePath = "uploads/" + fileName;
        logger.info("Downloading file: {}", filePath);
        FileInputStream fileInputStream = new FileInputStream(filePath);
        InputStreamResource resource = new InputStreamResource(fileInputStream);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> list() {
        List<ItemOrder> orders = itemOrderService.getAllOrders();
        if (orders != null) {
            logger.info("Fetched all orders");
            return ResponseEntity.status(HttpStatus.OK).body(orders);
        } else {
            logger.info("No orders found");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

    }

    @GetMapping("/getById/{orderId}")
    public ResponseEntity<?> getById(@PathVariable long orderId) {

        ItemOrder order = itemOrderService.getOrderById(orderId);
        try {
            if (order != null) {
                logger.info("Order fetched by ID: {}", orderId);
                return ResponseEntity.status(HttpStatus.OK).body(order);
            } else {
                logger.warn("Order not found by ID: {}", orderId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

            }
        } catch (OrderingNotFoundException e) {
            logger.error("Error fetching order by ID: {}", orderId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }


    @PatchMapping("/complete/{orderId}")
    public ResponseEntity<?> markAsCompleted(@PathVariable long orderId) {
        try {
            ItemOrder order = itemOrderService.markAsCompleted(orderId);
            logger.info("Order status updated successfully: {}", orderId);
            return ResponseEntity.status(HttpStatus.OK).body(order);
        } catch (Exception e) {
            logger.error("Order status update failed for order: {}", orderId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PatchMapping("/review/{orderId}")
    public ResponseEntity<?> markAsReviewed(@PathVariable long orderId) {
        try {
            ItemOrder order = itemOrderService.markAsReviewed(orderId);
            logger.info("Order status updated successfully: {}", orderId);
            return ResponseEntity.status(HttpStatus.OK).body(order);
        } catch (Exception e) {
            logger.error("Order status update failed for order: {}", orderId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PatchMapping("/problemReported/{orderId}")
    public ResponseEntity<?> markAsProblemReported(@PathVariable long orderId,@RequestBody Map<String, String> requestBody) {
        try {
            String note = requestBody.get("note");
            ItemOrder order = itemOrderService.markAsProblemReported(orderId,note);
            logger.info("Order status updated successfully: {}", orderId);
            return ResponseEntity.status(HttpStatus.OK).body(order);
        } catch (Exception e) {
            logger.error("Order status update failed for order: {}", orderId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PatchMapping("/cancel/{orderId}")
    public ResponseEntity<?> markAsCancel(@PathVariable long orderId) {
        try {
            ItemOrder order = itemOrderService.markAsCancelled(orderId);
            logger.info("Order status updated successfully: {}", orderId);
            return ResponseEntity.status(HttpStatus.OK).body(order);
        } catch (Exception e) {
            logger.error("Order status update failed for order: {}", orderId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PatchMapping("/resolve/{orderId}")
    public ResponseEntity<?> markAsResolve(@PathVariable long orderId) {
        try {
            ItemOrder order = itemOrderService.markAsResolved(orderId);
            logger.info("Order status updated successfully: {}", orderId);
            return ResponseEntity.status(HttpStatus.OK).body(order);
        } catch (Exception e) {
            logger.error("Order status update failed for order: {}", orderId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

//    @PutMapping("/updateById/{orderId}")
//    public ResponseEntity<?> updateOrder(@RequestBody @Valid ItemOrder newItemOrder, BindingResult bindingResult, @PathVariable long orderId) {
//        if (bindingResult.hasErrors()) {
//            logger.warn("Validation errors for order update: {}", newItemOrder.getOrderId());
//            Map<String, String> errors = bindingResult.getFieldErrors().stream()
//                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
//        }
//
//
//        itemOrderService.updateOrderById(newItemOrder, orderId);
//
//        ItemOrder itemOrder = itemOrderService.updateOrderById(newItemOrder, orderId);
//        logger.info("Order updated successfully: {}", orderId);
//        Long actorId = loginService.userId;
//        userActivityLogService.logUserActivity(actorId, itemOrder.getOrderId(), "Order Updated");
//
//        return ResponseEntity.status(HttpStatus.OK).body("Details were edited successfully");
//    }



//    @DeleteMapping("/deleteOrder/{orderId}")
//    public ResponseEntity<?> deleteOrder(@PathVariable long orderId) {
//        try {
//            String result = itemOrderService.deleteOrderById(orderId);
//            logger.info("Order deleted successfully: {}", orderId);
//            return ResponseEntity.status(HttpStatus.OK).body(result);
//        } catch (OrderNotFoundException e) {
//            logger.error("Order deletion failed for order: {} - Order not found", orderId, e);
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found with id: " + orderId);
//        } catch (IllegalStateException e) {
//            logger.error("Order deletion failed for order: {} - Order status is PENDING", orderId, e);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order with id " + orderId + " cannot be deleted because its status is PENDING");
//        } catch (Exception e) {
//            logger.error("Order deletion failed for order: {}", orderId, e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the order");
//        }
//
//    }


}

