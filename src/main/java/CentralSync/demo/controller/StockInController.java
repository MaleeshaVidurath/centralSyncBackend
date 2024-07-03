package CentralSync.demo.controller;

import CentralSync.demo.dto.MonthlyStockData;
import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.StockIn;
import CentralSync.demo.model.User;
import CentralSync.demo.repository.StockInRepository;
import CentralSync.demo.service.*;
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
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/stock-in")
@CrossOrigin(origins = "http://localhost:3000")
public class StockInController {

    @Autowired
    private StockInService stockInService;
    @Autowired
    private InventoryItemService inventoryItemService;
    @Autowired
    private StockInRepository stockInRepository;
    @Autowired
    private UserActivityLogService userActivityLogService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private StockService stockService;
    @Autowired
    private UserService userService;


    @PostMapping("/add")
    public ResponseEntity<?> createStockIn(@RequestParam("location") String location,
                                           @RequestParam("description") String description,
                                           @RequestParam("inQty") int inQty,
                                           @RequestParam("date") String date,
                                           @RequestParam("itemId") long itemId,
                                           @RequestParam("userId") long userId,
                                           @RequestParam(value = "file", required = false) MultipartFile file) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);

        String filePath = null;

        if (file != null && !file.isEmpty()) {
            try {
                // Save the file to a designated folder
                String uploadFolder = "uploads/";
                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadFolder + file.getOriginalFilename());
                Files.write(path, bytes);
                filePath = path.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>("Failed to upload file.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        InventoryItem inventoryItem = inventoryItemService.getItemById(itemId);
        if (inventoryItem == null) {
            return new ResponseEntity<>("Item not found", HttpStatus.NOT_FOUND);
        }
        if (!inventoryItemService.isActive(itemId)) {
            return new ResponseEntity<>("Inventory item is inactive and cannot be used", HttpStatus.FORBIDDEN);
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        StockIn stockIn = StockIn.builder()
                .location(location)
                .description(description)
                .inQty(inQty)
                .date(localDate)
                .filePath(filePath)
                .itemId(inventoryItem)
                .userId(user)
                .build();

        // Save the StockIn object to the database
        StockIn savedStockIn = stockInService.saveStockIn(stockIn);

        // Log user activity
        Long actorId = loginService.userId;
        userActivityLogService.logUserActivity(actorId, savedStockIn.getSinId(), "New Stock In added");

        // Update the quantity in InventoryItem
        inventoryItem.setQuantity(inventoryItem.getQuantity() + inQty);
        inventoryItemService.saveItem(inventoryItem);

        return new ResponseEntity<>(savedStockIn, HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public List<StockIn> list(@RequestParam(required = false) ItemGroupEnum itemGroup, @RequestParam(required = false) String year) {
        if (itemGroup != null && year != null) {
            return stockInService.getStockByGroupAndYear(itemGroup, year);
        } else {
            return stockInService.getAllStockIn();
        }

    }

    @GetMapping("/getById/{sinId}")
    public StockIn listById(@PathVariable long sinId) {
        return stockInService.getStockInById(sinId);
    }

    @PutMapping("/updateById/{sinId}")
    public StockIn updateStockIn(@RequestBody StockIn newStockIn, @PathVariable long sinId) {

        StockIn updatedStockIn= stockInService.updateStockInById(newStockIn, sinId);
        // Log user activity
        Long actorId=loginService.userId;
        userActivityLogService.logUserActivity(actorId,updatedStockIn.getSinId(), "New Stock In updated");
        return (updatedStockIn);
    }


    @DeleteMapping("/deleteById/{sinId}")
    public String deleteStockIn(@PathVariable long sinId) {
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

    @GetMapping("/api/stocks/monthly") // get stock-inventory in current year
    public Map<String, List<MonthlyStockData>> getMonthlyStockData() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return stockService.getMonthlyStockData(currentYear);
    }

}

