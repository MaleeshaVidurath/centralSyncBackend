package CentralSync.demo.controller;

import CentralSync.demo.model.StockOut;
import CentralSync.demo.service.StockOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stockout")
@CrossOrigin("http://localhost:3000")
public class StockOutController {
    @Autowired
    private StockOutService stockOutService;

    @PostMapping("/add")
    public String add(@RequestBody StockOut stockOut){
        stockOutService.saveStockOut(stockOut);
        return "New stock-out is added.";
    }

    @GetMapping("/getAll")
    public List<StockOut> getAllStockOut(){
        return stockOutService.getAllStockOut();
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