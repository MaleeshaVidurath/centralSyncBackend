package CentralSync.demo.controller;

import CentralSync.demo.Model.Ordering;
import CentralSync.demo.service.OrderingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderingController {

    @Autowired
    private OrderingService orderingService;

    @PostMapping("/add")
    public Ordering add(@RequestBody Ordering order){
        orderingService.saveNewOrder(order);
        return order;
    }

    @GetMapping("/getAll")
    public List<Ordering> list(){
        return orderingService.getAllOrders();
    }

    @GetMapping("/getById/{orderId}")
    public Ordering listById(@PathVariable long orderId){
        return orderingService.getOrderById(orderId);
    }

    @PutMapping("/updateById/{orderId}")
    public Ordering updateOrder(@RequestBody Ordering newOrder,@PathVariable long orderId){
        return orderingService.updateOrderById(newOrder ,orderId);
    }

    @DeleteMapping("/deleteOrder/{orderId}")

    public String deleteOrder(@PathVariable long orderId){
        return  orderingService.deleteOrderById(orderId);
    }





}

