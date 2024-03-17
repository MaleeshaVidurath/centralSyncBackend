package CentralSync.demo.service;

import CentralSync.demo.Model.StockOut;

import java.util.List;

public interface StockOutService {
    public StockOut saveStockOut(StockOut stockOut);

    public List<StockOut> getAllStockOut();
}
