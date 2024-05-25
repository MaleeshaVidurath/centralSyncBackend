package CentralSync.demo.controller;

import CentralSync.demo.model.OrderStatus;
import CentralSync.demo.model.ItemOrder;
import CentralSync.demo.service.EmailSenderService;
import CentralSync.demo.service.ItemOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import CentralSync.demo.service.UserActivityLogService;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController

@RequestMapping("/orders")
@CrossOrigin
public class ItemOrderController {

    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private ItemOrderService itemOrderService;
    @Autowired
    private UserActivityLogService userActivityLogService;


    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid ItemOrder itemOrder, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }

        itemOrder.setStatus(OrderStatus.PENDING);
        ItemOrder savedItemOrder = itemOrderService.saveNewOrder(itemOrder);


// Send email to the vendor
        String subject = "Order Request - " + savedItemOrder.getItemName();
        String body = "Dear Vendor,\n\n"
                + "We are interested in placing an order for the following item:\n\n"
                + "- Item Name: " + savedItemOrder.getItemName() + "\n"
                + "- Brand: " + savedItemOrder.getBrandName() + "\n"
                + "- Quantity: " + savedItemOrder.getQuantity() + "\n\n"
                + "Could you please confirm availability and provide us with pricing and lead time information?"
                + " Additionally, if there are any specific ordering requirements or forms we need to fill out, please let us know.\n\n"
                + "Thank you for your prompt attention to this matter. We look forward to your response.\n\n"
                + "Best regards,\n";
        emailSenderService.sendSimpleEmail(savedItemOrder.getVendorEmail(), subject, body);

        // Log user activity
        userActivityLogService.logUserActivity(savedItemOrder.getOrderId(), "New order added");


        return ResponseEntity.ok("Order initiated ");
    }

    @GetMapping("/getAll")
    public List<ItemOrder> list() {
        return itemOrderService.getAllOrders();
    }

    @GetMapping("/getById/{orderId}")
    public ItemOrder listById(@PathVariable long orderId) {
        return itemOrderService.getOrderById(orderId);
    }

    @PutMapping("/updateById/{orderId}")
    public ResponseEntity<?> updateOrder(@RequestBody @Valid ItemOrder newItemOrder, BindingResult bindingResult, @PathVariable long orderId) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }


        itemOrderService.updateOrderById(newItemOrder, orderId);

        ItemOrder itemOrder = itemOrderService.updateOrderById(newItemOrder, orderId);
        userActivityLogService.logUserActivity(itemOrder.getOrderId(), "Order Updated");

        return ResponseEntity.ok("Order details edited");
    }

    @PatchMapping("/updateStatus/{orderId}")
    public ItemOrder updateStatus(@PathVariable long orderId) {
        return itemOrderService.updateOrderStatus(orderId);
    }

    @DeleteMapping("/deleteOrder/{orderId}")
    public String deleteOrder(@PathVariable long orderId) {
        return itemOrderService.deleteOrderById(orderId);
    }


}

