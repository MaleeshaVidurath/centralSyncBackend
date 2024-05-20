package CentralSync.demo.service;

import CentralSync.demo.exception.StockInNotFoundException;
import CentralSync.demo.exception.StockOutNotFoundException;
import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.StockOut;
import CentralSync.demo.repository.StockOutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

        // Convert year to int
        int yearValue = Integer.parseInt(year);

        // Set start date to January 1st of the year and end date to December 31st of the year
        LocalDate startDate = LocalDate.of(yearValue, Month.JANUARY, 1);
        LocalDate endDate = LocalDate.of(yearValue, Month.DECEMBER, 31);

        List<StockOut> byYear = stockOutRepository.findAllByDateBetween(startDate,endDate);
        List<StockOut> byGroup = stockOutRepository.findAllByItemGroup(itemGroup);

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
