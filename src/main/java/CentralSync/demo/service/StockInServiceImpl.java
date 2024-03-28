package CentralSync.demo.service;

<<<<<<< HEAD
=======
import CentralSync.demo.exception.StockInNotFoundException;
>>>>>>> origin
import CentralSync.demo.model.StockIn;
import CentralSync.demo.repository.StockInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

<<<<<<< HEAD
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
=======

import java.util.List;
//import java.util.Optional;
@Service
public class StockInServiceImpl implements StockInService {

    @Autowired //ingect the repository
    private StockInRepository stockinRepository;

    @Override
    public StockIn saveStockIn(StockIn stockIn){
        return stockinRepository.save(stockIn);
>>>>>>> origin
    }

    @Override
    public List<StockIn> getAllStockIn() {
<<<<<<< HEAD
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


=======
        return stockinRepository.findAll();
    }

    @Override
    public StockIn getStockInById(long sinId) {
        return stockinRepository.findById(sinId)
                .orElseThrow(()-> new StockInNotFoundException(sinId));
    }

    @Override
    public StockIn updateStockInById(StockIn newStockIn, long sinId){
        return stockinRepository.findById(sinId)
                .map(stockIn -> {
                    stockIn.setDate(newStockIn.getDate());
                    stockIn.setLocation(newStockIn.getLocation());
                    stockIn.setInQty(newStockIn.getInQty());
                    stockIn.setDescription(newStockIn.getDescription());

                    return stockinRepository.save(stockIn);
                }).orElseThrow(()->new StockInNotFoundException(sinId));
    }
    @Override
    public String deleteStockInById(long sinId){
        if(!stockinRepository.existsById(sinId)){
            throw new StockInNotFoundException(sinId);
        }
        stockinRepository.deleteById(sinId);
        return  "StockIn with id "+sinId+" has been deleted successfully.";
>>>>>>> origin
    }

}
