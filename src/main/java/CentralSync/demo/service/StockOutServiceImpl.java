package CentralSync.demo.service;

import CentralSync.demo.model.StockIn;
import CentralSync.demo.model.StockOut;
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

    @Override
    public List<StockOut> getItemsByGroup(String itemGroup) {
        return stockOutRepository.findAllByItemGroup(itemGroup);
    }

    @Override
    public List<StockOut> getItemsByGroup_Year(String itemGroup,String year) {

        List<StockOut> byGroup = stockOutRepository.findAllByItemGroup(itemGroup);
        List<StockOut> byYear = stockOutRepository.findAllByDateContains(year);

        return byGroup.stream()
                .filter(byGroupItem -> byYear.stream()
                        .anyMatch(byYearItem -> byYearItem.getStockOutId() == byGroupItem.getStockOutId()))
                .toList();


    }
}
