package CentralSync.demo.controller;

import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.StockIn;
import CentralSync.demo.service.StockInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import CentralSync.demo.service.UserActivityLogService;

@RestController
@RequestMapping("/stock-in")
@CrossOrigin(origins = "http://localhost:3000")
public class StockInController {


    @Autowired
    private StockInService stockInService;
    @Autowired
    private UserActivityLogService userActivityLogService;

    @PostMapping("/add")

    public StockIn add(@RequestBody StockIn stockIn) {
        StockIn sin =stockInService.saveStockIn(stockIn);
        // Log user activity
        userActivityLogService.logUserActivity(sin.getSinId(), "New Stock In added");
        return stockIn;
    }


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

}

