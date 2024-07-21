package CentralSync.demo.service;

import CentralSync.demo.model.Adjustment;
import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.StockOut;
import CentralSync.demo.model.User;

import java.util.List;

public interface StockOutService {
    StockOut saveStockOut(StockOut stockOut);

    List<StockOut> getAllStockOut();

    List<StockOut> getItemsByGroupAndYear(ItemGroupEnum itemGroup, String year);

    StockOut getStockOutById(long soutId);
    StockOut updateStockOutById(StockOut newStockOut, long soutId);
    String deleteStockOutById(long soutId);

    List<StockOut> getStockOutsByUserId(User loggedUser);
}
