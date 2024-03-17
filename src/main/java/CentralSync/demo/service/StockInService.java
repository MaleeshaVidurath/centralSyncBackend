package CentralSync.demo.service;

import CentralSync.demo.Model.StockIn;

import java.util.List;

public interface StockInService {
    public StockIn saveStockIn(StockIn stockIn);

    public List<StockIn> getAllStockIn();
}
