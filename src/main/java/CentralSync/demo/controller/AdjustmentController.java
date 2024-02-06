package CentralSync.demo.controller;


import CentralSync.demo.Model.Adjustment;
import CentralSync.demo.exception.AdjustmentNotFoundException;
import CentralSync.demo.repository.AdjustmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdjustmentController {
    @Autowired
    private AdjustmentRepository adjustmentRepository;

    @PostMapping("/adjustment")
    Adjustment newAdjustment (@RequestBody Adjustment newAdjustment){
        return adjustmentRepository.save(newAdjustment);
    }

    @GetMapping("/adjustments")
    List<Adjustment> getAllAdjustments(){
        return adjustmentRepository.findAll();

    }

    @GetMapping("/adjustment/{adjId}")
    Adjustment getAdjustmentById(@PathVariable long adjId){
        return adjustmentRepository.findById(adjId)
                .orElseThrow(()-> new AdjustmentNotFoundException(adjId));
    }

    @PutMapping("/adjustment/{adjId}")
    Adjustment updateAdjustment(@RequestBody Adjustment newAdjustment, @PathVariable long adjId) {
        return adjustmentRepository.findById(adjId)
                .map(adjustment -> {
                    adjustment.setAdjStatus(newAdjustment.getAdjStatus());
                    adjustment.setReason(newAdjustment.getReason());
                    adjustment.setDate(newAdjustment.getDate());
                    adjustment.setDescription(newAdjustment.getDescription());
                    adjustment.setNewQuantity(newAdjustment.getNewQuantity());

                    return adjustmentRepository.save(adjustment);
                }).orElseThrow(()->new AdjustmentNotFoundException(adjId));
    }

    @DeleteMapping("/adjustment/{adjId}")
    String deleteAdjustment(@PathVariable long adjId){
        if(!adjustmentRepository.existsById(adjId)){
            throw new AdjustmentNotFoundException(adjId);
        }
        adjustmentRepository.deleteById(adjId);
        return  "Adjustment with id "+adjId+" has been deleted success.";
    }

}