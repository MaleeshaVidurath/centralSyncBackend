package CentralSync.demo.service;

import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.StockIn;
import CentralSync.demo.model.User;

import java.util.List;

public interface StockInService {
     StockIn saveStockIn(StockIn stockIn);

     List<StockIn> getAllStockIn();

     List<StockIn> getStockByGroupAndYear(ItemGroupEnum itemGroup,String year);
     StockIn getStockInById(long sinId);
     StockIn updateStockInById(StockIn newStockIn, long sinId);
     String deleteStockInById(long sinId);


    List<StockIn> getStockInByUserId(User loggedUser);
}
