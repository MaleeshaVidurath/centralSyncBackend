package CentralSync.demo.controller;

import CentralSync.demo.model.Adjustment;
import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.Status;
import CentralSync.demo.model.StockIn;
import CentralSync.demo.repository.StockInRepository;
import CentralSync.demo.service.StockInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import CentralSync.demo.service.UserActivityLogService;
import java.util.Optional;


@RestController
@RequestMapping("/stock-in")
@CrossOrigin(origins = "http://localhost:3000")
public class StockInController {


    @Autowired
    private StockInService stockInService;
    @Autowired
    private UserActivityLogService userActivityLogService;

    @Autowired
    private StockInRepository stockInRepository;

    @PostMapping("/add")
    public ResponseEntity<?> createStockIn(@RequestParam("location") String location,
                                              @RequestParam("description") String description,
                                              @RequestParam("inQty") int inQty,
                                              @RequestParam("date") String date,
                                              @RequestParam("itemId") long itemId,
                                              @RequestParam("file") MultipartFile file) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        try {
            // Save the file to a designated folder
            String uploadFolder = "uploads/";
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadFolder + file.getOriginalFilename());
            Files.write(path, bytes);

            StockIn stockIn = new StockIn();
            stockIn.setItemId(itemId);
            stockIn.setDescription(description);
            stockIn.setInQty(inQty);
            stockIn.setLocation(location);
            stockIn.setDate(localDate);
            stockIn.setFilePath(path.toString());

            // Save the Adjustment object to the database
            StockIn savedStockIn = stockInService.saveStockIn(stockIn);
            // Log user activity
            userActivityLogService.logUserActivity(savedStockIn.getSinId(), "New Stock In added");
            return new ResponseEntity<>(savedStockIn, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to upload file.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    @PostMapping("/add")
//    public StockIn add(@RequestBody StockIn stockIn) {
//        stockInService.saveStockIn(stockIn);
//        return stockIn;
//    }


    @GetMapping("/getAll")
    public  List<StockIn> listByCategory(@RequestParam(required = false) ItemGroupEnum itemGroup, @RequestParam(required = false) String year){
        if(itemGroup!=null && year!= null){
            return  stockInService.getStockByGroup_Year(itemGroup,year);
        }else{
            return stockInService.getAllStockIn();
        }

    }

    @GetMapping("/getById/{sinId}")
    public StockIn listById (@PathVariable long sinId){
        return stockInService.getStockInById(sinId);
    }

    @PutMapping("/updateById/{sinId}")
    public StockIn updateStockIn (@RequestBody StockIn newStockIn,@PathVariable long sinId){
        StockIn sin= stockInService.updateStockInById(newStockIn,sinId);
        userActivityLogService.logUserActivity(sin.getSinId(), "Stock In updated");
        return(newStockIn);

    }


    @DeleteMapping("/deleteById/{sinId}")
    public String deleteStockIn(@PathVariable long sinId){
        return stockInService.deleteStockInById(sinId);
    }

    @GetMapping("/getFileById/{sinId}")
    public ResponseEntity<UrlResource> downloadFile(@PathVariable Long sinId) {
        Optional<StockIn> stockInOptional = stockInRepository.findById(sinId);
        if (stockInOptional.isPresent()) {
            StockIn stockIn = stockInOptional.get();
            String filePath = stockIn.getFilePath();
            Path path = Paths.get(filePath);
            try {
                UrlResource resource = new UrlResource(path.toUri());
                if (Files.exists(path) && Files.isReadable(path)) {
                    return ResponseEntity.ok()
                            .header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
                            .body(resource);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

