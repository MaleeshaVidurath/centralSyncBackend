package CentralSync.demo.controller;

import CentralSync.demo.model.OrderStatus;
import CentralSync.demo.model.Ordering;
import CentralSync.demo.service.EmailSenderService;
import CentralSync.demo.service.OrderingService;
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
public class OrderingController {

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private OrderingService orderingService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid Ordering order, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }

        order.setStatus(OrderStatus.PENDING);
        Ordering savedOrder = orderingService.saveNewOrder(order);


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


        return ResponseEntity.ok("An order is initiated ");
    }

    @GetMapping("/getAll")
    public List<Ordering> list() {
        return orderingService.getAllOrders();
    }

    @GetMapping("/getById/{orderId}")
    public Ordering listById(@PathVariable long orderId) {
        return orderingService.getOrderById(orderId);
    }

    @PutMapping("/updateById/{orderId}")
    public ResponseEntity<?> updateOrder(@RequestBody @Valid Ordering newOrder, BindingResult bindingResult, @PathVariable long orderId) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }

        orderingService.updateOrderById(newOrder, orderId);
        return ResponseEntity.ok("Order details edited");
    }

    @PatchMapping("/updateStatus/{orderId}")
    public Ordering updateStatus(@PathVariable long orderId) {
        return orderingService.updateOrderStatus(orderId);
    }

    @DeleteMapping("/deleteOrder/{orderId}")
    public String deleteOrder(@PathVariable long orderId) {
        return orderingService.deleteOrderById(orderId);
    }


}

