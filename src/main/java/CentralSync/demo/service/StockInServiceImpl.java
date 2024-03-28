package CentralSync.demo.service;


import CentralSync.demo.exception.StockInNotFoundException;

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
                        .anyMatch(byYearItem -> byYearItem.getSinId() == byGroupItem.getSinId()))
                .toList();


    }

    @Override
    public StockIn getStockInById(long sinId) {
        return stockInRepository.findById(sinId)
                .orElseThrow(()-> new StockInNotFoundException(sinId));
    }

    @Override
    public StockIn updateStockInById(StockIn newStockIn, long sinId){
        return stockInRepository.findById(sinId)
                .map(stockIn -> {
                    stockIn.setDate(newStockIn.getDate());
                    stockIn.setLocation(newStockIn.getLocation());
                    stockIn.setInQty(newStockIn.getInQty());
                    stockIn.setDescription(newStockIn.getDescription());

                    return stockInRepository.save(stockIn);
                }).orElseThrow(()->new StockInNotFoundException(sinId));
    }
    @Override
    public String deleteStockInById(long sinId){
        if(!stockInRepository.existsById(sinId)){
            throw new StockInNotFoundException(sinId);
        }
        stockInRepository.deleteById(sinId);
        return  "StockIn with id "+sinId+" has been deleted successfully.";
    }

}
