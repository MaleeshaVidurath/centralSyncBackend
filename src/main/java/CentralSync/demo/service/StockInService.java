package CentralSync.demo.service;

import CentralSync.demo.model.StockIn;

import java.util.List;

public interface StockInService {
    public StockIn saveStockIn(StockIn stockIn);

    public List<StockIn> getAllStockIn();

    public StockIn getStockInById(long sinId);
    public StockIn updateStockInById(StockIn newStockIn, long sinId);
    public String deleteStockInById(long sinId);

}
