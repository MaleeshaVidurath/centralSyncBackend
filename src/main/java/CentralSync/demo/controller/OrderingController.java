package CentralSync.demo.controller;

import CentralSync.demo.Model.Ordering;
import CentralSync.demo.exception.OrderNotFoundException;
import CentralSync.demo.exception.OrderNotFoundException;
import CentralSync.demo.repository.OrderingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class OrderingController {
    @Autowired
    private OrderingRepository orderingRepository;

    @PostMapping("/ordering")
    Ordering newOrdering(@RequestBody Ordering newOrdering){
        return orderingRepository.save(newOrdering);

    }
    @GetMapping("/orderings")
    List<Ordering> getAllOrdering(){
        return orderingRepository.findAll();
    }

    @GetMapping("ordering/{id}")
    Ordering geOrderingById(@PathVariable Long id){
        return orderingRepository.findById(id)
                .orElseThrow(()->new OrderNotFoundException(id));
    }

    @PutMapping("/ordering/{id}")
    public Ordering updateOrdering(@RequestBody Ordering newOrdering, @PathVariable Long id) {
        return orderingRepository.findById(id)
                .map(ordering -> {
                    ordering.setVendorName(newOrdering.getVendorName());

                    return orderingRepository.save(ordering);
                })
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @DeleteMapping("/ordering/{id}")
    String deleteOrdering(@PathVariable Long id){
        if(!orderingRepository.existsById(id)){
            throw new OrderNotFoundException(id);
        }
        orderingRepository.deleteById(id);
        return "Ordering with id "+id+" has been deleted success";
    }



}

