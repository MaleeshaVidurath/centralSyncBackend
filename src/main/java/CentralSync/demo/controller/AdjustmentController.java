package CentralSync.demo.controller;

import CentralSync.demo.model.Adjustment;
import CentralSync.demo.model.Status;
import CentralSync.demo.service.AdjustmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/adjustment")
@CrossOrigin("http://localhost:3000")
public class AdjustmentController {
    @Autowired
    private AdjustmentService adjustmentService;

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody Adjustment adjustment){
        adjustment.setStatus(Status.PENDING);
        adjustmentService.saveAdjustment(adjustment);
       // return "New adjustment is added.";
        return new ResponseEntity<>(adjustment, HttpStatus.CREATED);
    }


    @GetMapping("/getAll")
    public List<Adjustment> getAllAdjustments(){
        return adjustmentService.getAllAdjustments();
    }

    @GetMapping("/getById/{adjId}")
    public Adjustment listById (@PathVariable Long adjId){

        return adjustmentService.getAdjustmentById(adjId);
    }

    @PutMapping("/updateById/{adjId}")
    public Adjustment updateAdjustment (@RequestBody Adjustment newAdjustment,@PathVariable Long adjId){
        return adjustmentService.updateAdjustmentById(newAdjustment,adjId);
    }

    @DeleteMapping("/deleteById/{adjId}")
    public String deleteAdjustment(@PathVariable Long adjId){
        return adjustmentService.deleteAdjustmentById(adjId);
    }

    @PatchMapping("/updateStatus/accept/{adjId}")
    public Adjustment updateStatusAccept(@PathVariable Long adjId) {
        return adjustmentService.updateAdjStatusAccept( adjId);
    }

    @PatchMapping("/updateStatus/reject/{adjId}")
    public Adjustment updateStatusReject(@PathVariable Long adjId) {
        return adjustmentService.updateAdjStatusReject( adjId);
    }

    @PostMapping("/{adjId}/upload")
    public ResponseEntity<String> uploadFile(@PathVariable Long adjId, @RequestParam("file") MultipartFile file) {
        try {
            adjustmentService.uploadFile(adjId, file);
            return ResponseEntity.ok("File uploaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
        }
    }

    @GetMapping("/{adjId}/download")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long adjId) {
        byte[] fileContent = adjustmentService.downloadFile(adjId);
        if (fileContent != null) {
            return ResponseEntity.ok().body(fileContent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}