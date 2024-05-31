package CentralSync.demo.service;

import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.StockIn;

import java.util.List;

public interface StockInService {
     StockIn saveStockIn(StockIn stockIn);

     List<StockIn> getAllStockIn();

     List<StockIn> getStockByGroup_Year(ItemGroupEnum itemGroup, String year);

     StockIn getStockInById(long sinId);
     StockIn updateStockInById(StockIn newStockIn, long sinId);
     String deleteStockInById(long sinId);


}
