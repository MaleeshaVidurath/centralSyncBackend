
package CentralSync.demo.service;

import CentralSync.demo.dto.MonthlyStockData;
import CentralSync.demo.repository.StockInRepository;
import CentralSync.demo.repository.StockOutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StockService {
    @Autowired
    private StockInRepository stockInRepository;

    @Autowired
    private StockOutRepository stockOutRepository;

    public Map<String, List<MonthlyStockData>> getMonthlyStockData(int year) {
        List<MonthlyStockData> stockInData = stockInRepository.findMonthlyStockIn(year);
        List<MonthlyStockData> stockOutData = stockOutRepository.findMonthlyStockOut(year);

        Map<String, List<MonthlyStockData>> result = new HashMap<>();
        result.put("stockIn", stockInData);
        result.put("stockOut", stockOutData);

        return result;
    }
}
