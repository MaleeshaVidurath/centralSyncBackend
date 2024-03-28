package CentralSync.demo.controller;


import CentralSync.demo.model.StockIn;
import CentralSync.demo.model.StockOut;
import CentralSync.demo.service.StockOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock-out")
@CrossOrigin
public class StockOutController {

    @Autowired
    private StockOutService stockOutService;

    @PostMapping("/add")
    public StockOut add(@RequestBody StockOut stockOut) {
        stockOutService.saveStockOut(stockOut);
        return stockOut;
    }

    @GetMapping("/getAll")
    public  List<StockOut> listByCategory(@RequestParam(required = false) String itemGroup, @RequestParam(required = false) String year){
        if(itemGroup!=null && year!= null){
            return  stockOutService.getItemsByGroup_Year(itemGroup,year);
        }else{
            return stockOutService.getAllStockOut();
        }

    }
}
