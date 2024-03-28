package CentralSync.demo.service;

import CentralSync.demo.model.StockIn;

import java.util.List;

public interface StockInService {
    public StockIn saveStockIn(StockIn stockIn);

    public List<StockIn> getAllStockIn();

<<<<<<< HEAD
    public List<StockIn> getItemsByGroup_Year(String itemGroup, String year);
=======
    public StockIn getStockInById(long sinId);
    public StockIn updateStockInById(StockIn newStockIn, long sinId);
    public String deleteStockInById(long sinId);

>>>>>>> origin
}
