package CentralSync.demo.service;

import CentralSync.demo.dto.LowStockItemDTO;
import CentralSync.demo.repository.InventoryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LowStockItemService {

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    public List<LowStockItemDTO> getLowStockItems() {
        return inventoryItemRepository.findLowStockItems();
    }
}
