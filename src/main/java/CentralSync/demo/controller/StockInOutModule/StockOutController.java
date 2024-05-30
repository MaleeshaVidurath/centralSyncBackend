package CentralSync.demo.controller.StockInOutModule;

import CentralSync.demo.model.InventoryItemModule.InventoryItem;
import CentralSync.demo.model.InventoryItemModule.ItemGroupEnum;
import CentralSync.demo.model.StockInStockOutModule.StockOut;
import CentralSync.demo.service.InventoryItemModule.InventoryItemService;
import CentralSync.demo.service.StockInOutModule.StockOutService;
import CentralSync.demo.service.UserManagementModule.UserActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

@RestController
@RequestMapping("/stock-out")
@CrossOrigin
public class StockOutController {

    @Autowired
    private StockOutService stockOutService;
    @Autowired
    private UserActivityLogService userActivityLogService;

    @Autowired
    private InventoryItemService inventoryItemService;

    @PostMapping("/add")
    public ResponseEntity<?> createStockOut(@RequestParam("department") String department,
                                           @RequestParam("description") String description,
                                           @RequestParam("outQty") int outQty,
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

            StockOut stockOut = new StockOut();
            stockOut.setItemId(itemId);
            stockOut.setDescription(description);
            stockOut.setOutQty(outQty);
            stockOut.setDepartment(department);
            stockOut.setDate(localDate);
            stockOut.setFilePath(path.toString());

            // Save the Adjustment object to the database
            StockOut savedStockOut = stockOutService.saveStockOut(stockOut);
            //Log User Activity
            userActivityLogService.logUserActivity(savedStockOut.getSoutId(), "New Stock Out added");

            // Update the quantity in InventoryItem
            InventoryItem inventoryItem = inventoryItemService.getItemById(itemId);
            if (inventoryItem != null) {
                inventoryItem.setQuantity(inventoryItem.getQuantity() - outQty);
                inventoryItemService.saveItem(inventoryItem);
            } else {
                return new ResponseEntity<>("Item not found", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(savedStockOut, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to upload file.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/getAll")
    public  List<StockOut> listByCategory(@RequestParam(required = false) ItemGroupEnum itemGroup, @RequestParam(required = false) String year){
        if(itemGroup!=null && year!= null){
            return  stockOutService.getItemsByGroup_Year(itemGroup,year);
        }else{
            return stockOutService.getAllStockOut();
        }

    }

    @GetMapping("/getById/{soutId}")
    public StockOut listById (@PathVariable long soutId){
        return stockOutService.getStockOutById(soutId);
    }

    @PutMapping("/updateById/{soutId}")
    public StockOut updateStockOut (@RequestBody StockOut newStockOut,@PathVariable long soutId){
        StockOut sout= stockOutService.updateStockOutById(newStockOut,soutId);
        //Log User Activity
        userActivityLogService.logUserActivity(sout.getSoutId(), " Stock Out updated");
        return newStockOut;
    }


    @DeleteMapping("/deleteById/{soutId}")
    public String deleteStockOut(@PathVariable long soutId){
        return stockOutService.deleteStockOutById(soutId);
    }
}