package CentralSync.demo.controller;

import CentralSync.demo.model.OrderStatus;
import CentralSync.demo.model.Order;
import CentralSync.demo.service.EmailSenderService;
import CentralSync.demo.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController

@RequestMapping("/orders")
@CrossOrigin
public class OrderController {

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid Order order, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }

        order.setStatus(OrderStatus.PENDING);
        Order savedOrder = orderService.saveNewOrder(order);


// Send email to the vendor
        String subject = "Order Request - " + savedOrder.getItemName();
        String body = "Dear Vendor,\n\n"
                + "We are interested in placing an order for the following item:\n\n"
                + "- Item Name: " + savedOrder.getItemName() + "\n"
                + "- Brand: " + savedOrder.getBrandName() + "\n"
                + "- Quantity: " + savedOrder.getQuantity() + "\n\n"
                + "Could you please confirm availability and provide us with pricing and lead time information?"
                + " Additionally, if there are any specific ordering requirements or forms we need to fill out, please let us know.\n\n"
                + "Thank you for your prompt attention to this matter. We look forward to your response.\n\n"
                + "Best regards,\n";
        emailSenderService.sendSimpleEmail(savedOrder.getVendorEmail(), subject, body);


        return ResponseEntity.ok("Order is initiated ");
    }

    @GetMapping("/getAll")
    public List<Order> list() {
        return orderService.getAllOrders();
    }

    @GetMapping("/getById/{orderId}")
    public Order listById(@PathVariable long orderId) {
        return orderService.getOrderById(orderId);
    }

    @PutMapping("/updateById/{orderId}")
    public ResponseEntity<?> updateOrder(@RequestBody @Valid Order newOrder, BindingResult bindingResult, @PathVariable long orderId) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }

        orderService.updateOrderById(newOrder, orderId);
        return ResponseEntity.ok("Order details edited");
    }

    @PatchMapping("/updateStatus/{orderId}")
    public Order updateStatus(@PathVariable long orderId) {
        return orderService.updateOrderStatus(orderId);
    }

    @DeleteMapping("/deleteOrder/{orderId}")
    public String deleteOrder(@PathVariable long orderId) {
        return orderService.deleteOrderById(orderId);
    }


}

