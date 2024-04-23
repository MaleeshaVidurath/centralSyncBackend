package CentralSync.demo.service;


import CentralSync.demo.exception.StockInNotFoundException;

import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.StockIn;
import CentralSync.demo.repository.StockInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
    public List<StockIn> getStockByGroup_Year(ItemGroupEnum itemGroup, String year) {

        // Convert year to int
        int yearValue = Integer.parseInt(year);

        // Set start date to January 1st of the year and end date to December 31st of the year
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, yearValue);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();

        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        Date endDate = calendar.getTime();


        List<StockIn> byYear = stockInRepository.findAllByDateBetween(startDate,endDate);
        List<StockIn> byGroup = stockInRepository.findAllByItemGroup(itemGroup);

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
