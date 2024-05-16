package CentralSync.demo.controller;

import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.StockOut;
import CentralSync.demo.service.StockOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import CentralSync.demo.service.UserActivityLogService;

@RestController
@RequestMapping("/stock-out")
@CrossOrigin
public class StockOutController {

    @Autowired
    private StockOutService stockOutService;
    @Autowired
    private UserActivityLogService userActivityLogService;

    @PostMapping("/add")
    public StockOut add(@RequestBody StockOut stockOut) {
        stockOutService.saveStockOut(stockOut);
        return stockOut;
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
        return stockOutService.updateStockOutById(newStockOut,soutId);
    }


    @DeleteMapping("/deleteById/{soutId}")
    public String deleteStockOut(@PathVariable long soutId){
        return stockOutService.deleteStockOutById(soutId);
    }
}
