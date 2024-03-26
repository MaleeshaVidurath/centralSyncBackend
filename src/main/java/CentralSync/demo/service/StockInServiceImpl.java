package CentralSync.demo.service;

import CentralSync.demo.exception.StockInNotFoundException;
import CentralSync.demo.model.StockIn;
import CentralSync.demo.repository.StockInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
//import java.util.Optional;
@Service
public class StockInServiceImpl implements StockInService {

    @Autowired //ingect the repository
    private StockInRepository stockinRepository;

    @Override
    public StockIn saveStockIn(StockIn stockIn){
        return stockinRepository.save(stockIn);
    }

    @Override
    public List<StockIn> getAllStockIn() {
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
    }

}
