package CentralSync.demo.controller;

import CentralSync.demo.dto.RecentlyUsedItemDTO;
import CentralSync.demo.model.*;
import CentralSync.demo.repository.StockOutRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stock-out")
@CrossOrigin
public class StockOutController {

    @Autowired
    private StockOutService stockOutService;

    @Autowired
    private InventoryItemService inventoryItemService;

    @Autowired
    private StockOutRepository stockOutRepository;
    @Autowired
    private UserActivityLogService userActivityLogService;
    @Autowired
    private StockService stockService;
    @Autowired
    private UserService userService;
    @Autowired
    private LoginService loginService;
    @PostMapping("/add")
    public ResponseEntity<?> createStockOut(@RequestParam("department") String department,
                                            @RequestParam("description") String description,
                                            @RequestParam("outQty") int outQty,
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

        StockOut stockOut = StockOut.builder()
                .department(department)
                .description(description)
                .outQty(outQty)
                .date(localDate)
                .filePath(filePath)
                .itemId(inventoryItem)
                .userId(user)
                .build();

        // Save the StockOut object to the database
        StockOut savedStockOut = stockOutService.saveStockOut(stockOut);

        // Log user activity
        Long actorId = loginService.userId;
        userActivityLogService.logUserActivity(actorId, savedStockOut.getSoutId(), "New Stock Out added");

        // Update the quantity in InventoryItem
        inventoryItem.setQuantity(inventoryItem.getQuantity() - outQty);
        inventoryItemService.saveItem(inventoryItem);

        return new ResponseEntity<>(savedStockOut, HttpStatus.CREATED);
    }


    @GetMapping("/getAll")
    public  List<StockOut> listByCategory(@RequestParam(required = false) ItemGroupEnum itemGroup, @RequestParam(required = false) String year){
        if(itemGroup!=null && year!= null){
            return  stockOutService.getItemsByGroupAndYear(itemGroup,year);
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
        Long actorId=loginService.userId;
        userActivityLogService.logUserActivity(actorId,sout.getSoutId(), "Stock Out updated");
        return newStockOut;
    }

    @GetMapping("/getFileById/{soutId}")
    public ResponseEntity<UrlResource> downloadFile(@PathVariable Long soutId) {
        Optional<StockOut> stockOutOptional = stockOutRepository.findById(soutId);
        if (stockOutOptional.isPresent()) {
            StockOut stockOut = stockOutOptional.get();
            String filePath = stockOut.getFilePath();
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


    @DeleteMapping("/deleteById/{soutId}")
    public String deleteStockOut(@PathVariable long soutId){
        return stockOutService.deleteStockOutById(soutId);
    }

    @GetMapping("/recently-used")
    public List<RecentlyUsedItemDTO> getRecentlyUsedItems() {
        List<Object[]> results = stockService.getRecentlyUsedItems();
        return results.stream()
                .map(result -> new RecentlyUsedItemDTO((Long) result[0], (String) result[2], (Long) result[1]))
                .collect(Collectors.toList());
    }
}
