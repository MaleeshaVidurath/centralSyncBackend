package CentralSync.demo.controller;

import CentralSync.demo.dto.LowStockItemDTO;
import CentralSync.demo.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/low-stock-items")
public class LowStockItemController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    public ResponseEntity<List<LowStockItemDTO>> getLowStockItems() {
        List<LowStockItemDTO> lowStockItems = reportService.getLowStockItems();
        return ResponseEntity.ok(lowStockItems);
    }
}
