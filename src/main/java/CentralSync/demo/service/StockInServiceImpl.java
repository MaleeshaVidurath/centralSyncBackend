package CentralSync.demo.service;


import CentralSync.demo.exception.StockInNotFoundException;

import CentralSync.demo.model.Adjustment;
import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.StockIn;
import CentralSync.demo.repository.InventoryItemRepository;
import CentralSync.demo.repository.StockInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StockInServiceImpl implements StockInService {

    @Autowired
    private StockInRepository stockInRepository;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Override
    public StockIn saveStockIn(StockIn stockIn) {
        return stockInRepository.save(stockIn);
    }


    @Override
    public List<StockIn> getAllStockIn() {

        return stockInRepository.findAll();
    }


    @Override
    public StockIn getStockInById(long sinId) {
        return stockInRepository.findById(sinId)
                .orElseThrow(() -> new StockInNotFoundException(sinId));
    }

    @Override
    public StockIn updateStockInById(StockIn newStockIn, long sinId) {
        return stockInRepository.findById(sinId)
                .map(stockIn -> {
                    stockIn.setDate(newStockIn.getDate());
                    stockIn.setLocation(newStockIn.getLocation());
                    stockIn.setInQty(newStockIn.getInQty());
                    stockIn.setDescription(newStockIn.getDescription());

                    return stockInRepository.save(stockIn);
                }).orElseThrow(() -> new StockInNotFoundException(sinId));
    }

    @Override
    public String deleteStockInById(long sinId) {
        if (!stockInRepository.existsById(sinId)) {
            throw new StockInNotFoundException(sinId);
        }
        stockInRepository.deleteById(sinId);
        return "StockIn with id " + sinId + " has been deleted successfully.";
    }

    @Override
    public List<StockIn> getStockByGroupAndYear(ItemGroupEnum itemGroup, String year) {

        int yearInt = Integer.parseInt(year);
        List<StockIn> byYear = stockInRepository.stockInByYear(yearInt);

        //filter by item group
        List<InventoryItem> items = inventoryItemRepository.findAllByItemGroup(itemGroup);
        List<Long> itemIds = items.stream()
                .map(InventoryItem::getItemId)
                .collect(Collectors.toList());
        List<StockIn> byGroup = stockInRepository.findAllByItemIdIn(itemIds);

        //filtered by year and group
        return byGroup.stream()
                .filter(byGroupItem -> byYear.stream()
                        .anyMatch(byYearItem -> byYearItem.getSinId() == byGroupItem.getSinId()))
                .toList();

    }

}
