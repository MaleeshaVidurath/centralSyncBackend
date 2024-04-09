package CentralSync.demo.controller;

import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.StockIn;
import CentralSync.demo.service.StockInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/stock-in")
@CrossOrigin(origins = "http://localhost:3000")
public class StockInController {


    @Autowired
    private StockInService stockInService;

    @PostMapping("/add")

    public StockIn add(@RequestBody StockIn stockIn) {
        stockInService.saveStockIn(stockIn);
        return stockIn;
    }


    @GetMapping("/getAll")
    public  List<StockIn> listByCategory(@RequestParam(required = false) ItemGroupEnum itemGroup, @RequestParam(required = false) String year){
        if(itemGroup!=null && year!= null){
            return  stockInService.getItemsByGroup_Year(itemGroup,year);
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
        return stockInService.updateStockInById(newStockIn,sinId);
    }


    @DeleteMapping("/deleteById/{sinId}")
    public String deleteStockIn(@PathVariable long sinId){
        return stockInService.deleteStockInById(sinId);
    }

}

