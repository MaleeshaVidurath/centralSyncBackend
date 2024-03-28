package CentralSync.demo.service;

import CentralSync.demo.model.StockIn;
import CentralSync.demo.repository.StockInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<StockIn> getItemsByGroup_Year(String itemGroup,String year) {

        List<StockIn> byGroup = stockInRepository.findAllByItemGroup(itemGroup);
        List<StockIn> byYear = stockInRepository.findAllByDateContains(year);

        return byGroup.stream()
                .filter(byGroupItem -> byYear.stream()
                        .anyMatch(byYearItem -> byYearItem.getStockInId() == byGroupItem.getStockInId()))
                .toList();


    }

}
