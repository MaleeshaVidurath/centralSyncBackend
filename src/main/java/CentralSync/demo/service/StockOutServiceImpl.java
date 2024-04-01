package CentralSync.demo.service;

import CentralSync.demo.exception.StockOutNotFoundException;
import CentralSync.demo.model.StockOut;
import CentralSync.demo.repository.StockOutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
//import java.util.Optional;
@Service
public class StockOutServiceImpl implements StockOutService {

    @Autowired //ingect the repository
    private StockOutRepository stockoutRepository;

    @Override
    public StockOut saveStockOut(StockOut stockOut){
        return stockoutRepository.save(stockOut);
    }

    @Override
    public List<StockOut> getAllStockOut() {
        return stockoutRepository.findAll();
    }

    @Override
    public StockOut getStockOutById(long soutId) {
        return stockoutRepository.findById(soutId)
                .orElseThrow(()-> new StockOutNotFoundException(soutId));
    }

    @Override
    public StockOut updateStockOutById(StockOut newStockOut, long soutId){
        return stockoutRepository.findById(soutId)
                .map(stockOut -> {
                    stockOut.setDepartment(newStockOut.getDepartment());
                    stockOut.setOutQty(newStockOut.getOutQty());
                    stockOut.setDate(newStockOut.getDate());
                    stockOut.setDescription(newStockOut.getDescription());

                    return stockoutRepository.save(stockOut);
                }).orElseThrow(()->new StockOutNotFoundException(soutId));
    }
    @Override
    public String deleteStockOutById(long soutId){
        if(!stockoutRepository.existsById(soutId)){
            throw new StockOutNotFoundException(soutId);
        }
        stockoutRepository.deleteById(soutId);
        return  "StockOut with id "+soutId+" has been deleted successfully.";
    }

}
