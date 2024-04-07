package CentralSync.demo.service;

import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.StockIn;
import CentralSync.demo.model.StockOut;

import java.util.List;

public interface StockOutService {
    public StockOut saveStockOut(StockOut stockOut);

    public List<StockOut> getAllStockOut();

    public List<StockOut> getItemsByGroup_Year(ItemGroupEnum itemGroup, String year);
}
