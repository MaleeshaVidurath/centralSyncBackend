package CentralSync.demo.controller;

import CentralSync.demo.model.OrderStatus;
import CentralSync.demo.model.Ordering;
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
    private OrderingService orderingService;

    @PostMapping("/add")

    public ResponseEntity<?> add(@RequestBody @Valid Ordering order, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }

        order.setStatus(OrderStatus.PENDING);
        orderingService.saveNewOrder(order);
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
    public Ordering updateOrder(@RequestBody Ordering newOrder, @PathVariable long orderId) {
        return orderingService.updateOrderById(newOrder, orderId);
    }

    @PatchMapping("/updateStatus/{orderId}")
    public Ordering updateStatus( @PathVariable long orderId) {
        return orderingService.updateOrderStatus( orderId);
    }

    @DeleteMapping("/deleteOrder/{orderId}")
    public String deleteOrder(@PathVariable long orderId) {
        return orderingService.deleteOrderById(orderId);
    }


}

