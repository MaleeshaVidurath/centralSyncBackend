package CentralSync.demo.controller;

import CentralSync.demo.model.*;
import CentralSync.demo.service.ItemOrderService;
import CentralSync.demo.service.LoginService;
import CentralSync.demo.service.UserActivityLogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class ItemOrderController {


    @Autowired
    private ItemOrderService itemOrderService;
    @Autowired
    private UserActivityLogService userActivityLogService;
    @Autowired
    private LoginService loginService;


    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestPart("order") @Valid ItemOrder itemOrder,
                                 @RequestPart(value = "file", required = false) MultipartFile file,
                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }
        if (file != null && !file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get("uploads/" + file.getOriginalFilename());
                Files.write(path, bytes);
                itemOrder.setFilePath(path.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
            }
        }

        itemOrder.setStatus(OrderStatus.PENDING);

        itemOrderService.saveNewOrder(itemOrder);

//        Log user activity
//        Long actorId = loginService.userId;
//        userActivityLogService.logUserActivity(actorId, savedItemOrder.getOrderId(), "New order added");


        return ResponseEntity.status(HttpStatus.CREATED).body("Order initiated");
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<InputStreamResource> downloadPdf(@PathVariable String fileName) throws IOException {
        String filePath = "uploads/" + fileName;
        FileInputStream fileInputStream = new FileInputStream(filePath);
        InputStreamResource resource = new InputStreamResource(fileInputStream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> list() {
        List<ItemOrder> orders = itemOrderService.getAllOrders();
        if (orders != null) {
            return ResponseEntity.status(HttpStatus.OK).body(orders);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

    }

    @GetMapping("/getById/{orderId}")
    public ResponseEntity<?> getById(@PathVariable long orderId) {

        ItemOrder order = itemOrderService.getOrderById(orderId);
        if (order != null) {
            return ResponseEntity.status(HttpStatus.OK).body(order);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        }

    }

    @PutMapping("/updateById/{orderId}")
    public ResponseEntity<?> updateOrder(@RequestBody @Valid ItemOrder newItemOrder, BindingResult bindingResult, @PathVariable long orderId) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }


        itemOrderService.updateOrderById(newItemOrder, orderId);

        ItemOrder itemOrder = itemOrderService.updateOrderById(newItemOrder, orderId);
        Long actorId = loginService.userId;
        userActivityLogService.logUserActivity(actorId, itemOrder.getOrderId(), "Order Updated");

        return ResponseEntity.status(HttpStatus.OK).body("Details were edited successfully");
    }

    @PatchMapping("/updateStatus/{orderId}")
    public ResponseEntity<?> updateStatus(@PathVariable long orderId) {
        try {
            ItemOrder status = itemOrderService.updateOrderStatus(orderId);
            return ResponseEntity.status(HttpStatus.OK).body(status);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/deleteOrder/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable long orderId) {
        try {
            String result = itemOrderService.deleteOrderById(orderId);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }


}

