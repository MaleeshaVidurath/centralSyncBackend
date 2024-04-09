package CentralSync.demo.service;

import CentralSync.demo.exception.StockInNotFoundException;
import CentralSync.demo.exception.StockOutNotFoundException;
import CentralSync.demo.model.ItemGroupEnum;
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
    public List<StockOut> getItemsByGroup_Year(ItemGroupEnum itemGroup, String year) {

        List<StockOut> byGroup = stockOutRepository.findAllByItemGroup(itemGroup);
        List<StockOut> byYear = stockOutRepository.findAllByDateContains(year);

        return byGroup.stream()
                .filter(byGroupItem -> byYear.stream()
                        .anyMatch(byYearItem -> byYearItem.getSoutId() == byGroupItem.getSoutId()))
                .toList();
    }

    @Override
    public StockOut getStockOutById(long soutId) {
        return stockOutRepository.findById(soutId)
                .orElseThrow(()-> new StockInNotFoundException(soutId));
    }

    @Override
    public StockOut updateStockOutById(StockOut newStockOut, long soutId){
        return stockOutRepository.findById(soutId)
                .map(stockOut -> {
                    stockOut.setDate(newStockOut.getDate());
                    stockOut.setOutQty(newStockOut.getOutQty());
                    stockOut.setDepartment(newStockOut.getDepartment());
                    stockOut.setDescription(newStockOut.getDescription());
                    return stockOutRepository.save(stockOut);
                }).orElseThrow(()->new StockOutNotFoundException(soutId));
    }
    @Override
    public String deleteStockOutById(long soutId){
        if(!stockOutRepository.existsById(soutId)){
            throw new StockOutNotFoundException(soutId);
        }
        stockOutRepository.deleteById(soutId);
        return  "StockOut with id "+soutId+" has been deleted successfully.";
    }
}
