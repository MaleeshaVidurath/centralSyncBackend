package CentralSync.demo.controller;

import CentralSync.demo.model.StockIn;
import CentralSync.demo.service.StockInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stockin")
@CrossOrigin("http://localhost:3000")
public class StockInController {
    @Autowired
    private StockInService stockInService;

    @PostMapping("/add")
    public String add(@RequestBody StockIn stockIn){
        stockInService.saveStockIn(stockIn);
        return "New stock-in is added.";
    }

    @GetMapping("/getAll")
    public List<StockIn> getAllStockIn(){
        return stockInService.getAllStockIn();
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