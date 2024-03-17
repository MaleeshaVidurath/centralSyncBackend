package CentralSync.demo.controller;

import CentralSync.demo.Model.StockOut;
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
    public List<StockOut> list() {
        return stockOutService.getAllStockOut();
    }
}
