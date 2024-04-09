package CentralSync.demo.service;

import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.StockIn;
import CentralSync.demo.model.StockOut;

import java.util.List;

public interface StockOutService {
    StockOut saveStockOut(StockOut stockOut);

    List<StockOut> getAllStockOut();

    List<StockOut> getItemsByGroup_Year(ItemGroupEnum itemGroup, String year);

    StockOut getStockOutById(long soutId);
    StockOut updateStockOutById(StockOut newStockOut, long soutId);
    String deleteStockOutById(long soutId);
}
