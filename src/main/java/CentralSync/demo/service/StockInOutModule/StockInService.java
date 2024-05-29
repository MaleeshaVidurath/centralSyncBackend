package CentralSync.demo.service.StockInOutModule;

import CentralSync.demo.model.InventoryItemModule.ItemGroupEnum;
import CentralSync.demo.model.StockInStockOutModule.StockIn;

import java.util.List;

public interface StockInService {
     StockIn saveStockIn(StockIn stockIn);

     List<StockIn> getAllStockIn();

     List<StockIn> getStockByGroup_Year(ItemGroupEnum itemGroup, String year);

     StockIn getStockInById(long sinId);
     StockIn updateStockInById(StockIn newStockIn, long sinId);
     String deleteStockInById(long sinId);


}
