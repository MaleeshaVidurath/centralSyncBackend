package CentralSync.demo.service;

import CentralSync.demo.Model.StockIn;
import CentralSync.demo.repository.StockInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockInServiceImpl implements StockInService {

    @Autowired
    private StockInRepository stockInRepository;
    @Override
    public StockIn saveStockIn(StockIn stockIn) {
        return stockInRepository.save(stockIn);
    }

    @Override
    public List<StockIn> getAllStockIn() {
        return stockInRepository.findAll();
    }
}
