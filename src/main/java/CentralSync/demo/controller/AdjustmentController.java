package CentralSync.demo.controller;

import CentralSync.demo.model.Adjustment;
import CentralSync.demo.service.AdjustmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adjustment")
@CrossOrigin("http://localhost:3000")
public class AdjustmentController {
    @Autowired
    private AdjustmentService adjustmentService;

    @PostMapping("/add")
    public String add(@RequestBody Adjustment adjustment){
        adjustmentService.saveAdjustment(adjustment);
        return "New adjustment is added.";
    }

    @GetMapping("/getAll")
    public List<Adjustment> getAllAdjustments(){
        return adjustmentService.getAllAdjustments();
    }

    @GetMapping("/getById/{adjId}")
    public Adjustment listById (@PathVariable long adjId){
        return adjustmentService.getAdjustmentById(adjId);
    }

    @PutMapping("/updateById/{adjId}")
    public Adjustment updateAdjustment (@RequestBody Adjustment newAdjustment,@PathVariable long adjId){
        return adjustmentService.updateAdjustmentById(newAdjustment,adjId);
    }


    @DeleteMapping("/deleteById/{adjId}")
    public String deleteAdjustment(@PathVariable long adjId){
        return adjustmentService.deleteAdjustmentById(adjId);
    }

    //handle the file upload



}