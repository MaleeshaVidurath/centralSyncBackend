package CentralSync.demo.service;

import CentralSync.demo.Model.StockOut;
import CentralSync.demo.repository.StockOutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockOutServiceImpl implements StockOutService {

    @Autowired
    private StockOutRepository stockOutRepository;

    @Override
    public StockOut saveStockOut(StockOut stockOut) {
        return stockOutRepository.save(stockOut);
    }

    @Override
    public List<StockOut> getAllStockOut() {
        return stockOutRepository.findAll();
    }
}
