package CentralSync.demo.controller;

import CentralSync.demo.Model.StockIn;
import CentralSync.demo.service.StockInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock-in")
@CrossOrigin
public class StockInController {

    @Autowired
    private StockInService stockInService;

    @PostMapping("/add")
    public StockIn add(@RequestBody StockIn stockIn) {
        stockInService.saveStockIn(stockIn);
        return stockIn;
    }

    @GetMapping("/getAll")
    public List<StockIn> list() {
        return stockInService.getAllStockIn();
    }
}
