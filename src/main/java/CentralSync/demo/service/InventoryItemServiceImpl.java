package CentralSync.demo.service;

import CentralSync.demo.dto.InventorySummaryDto;
import CentralSync.demo.dto.LowStockItemDTO;
import CentralSync.demo.exception.InventoryItemInUseException;
import CentralSync.demo.exception.InventoryItemNotFoundException;
import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.StatusEnum;
import CentralSync.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryItemServiceImpl implements InventoryItemService {


    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryRequestRepository inventoryRequestRepository;
    private final ReservationRepository reservationRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    private StockInRepository stockInRepository;

    @Autowired
    private StockOutRepository stockOutRepository;

    @Autowired
    public InventoryItemServiceImpl(
            InventoryItemRepository inventoryItemRepository,
            InventoryRequestRepository inventoryRequestRepository,
            ReservationRepository reservationRepository,
            TicketRepository ticketRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.inventoryRequestRepository = inventoryRequestRepository;
        this.reservationRepository = reservationRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public InventoryItem findDuplicateItem(InventoryItem inventoryItem) {

        // Check if an item with the same unique attributes already exists
        InventoryItem duplicateItem= inventoryItemRepository.findDuplicate(inventoryItem.getItemName(), inventoryItem.getBrand(),
                inventoryItem.getModel(), inventoryItem.getItemGroup());
        if (duplicateItem != null && duplicateItem.getItemId()!=inventoryItem.getItemId()){
            return duplicateItem;
        }

        return null;
    }

    @Override
    public InventoryItem saveItem(InventoryItem inventoryItem) {
        return inventoryItemRepository.save(inventoryItem);
    }

    @Override
    public List<InventoryItem> getAllItems() {
        return inventoryItemRepository.findAll();
    }

    @Override
    public InventoryItem getItemById(long itemId) {
        return inventoryItemRepository.findById(itemId)
                .orElseThrow(() -> new InventoryItemNotFoundException(itemId));
    }


    @Override
    public InventoryItem updateItemById(InventoryItem newInventoryItem, long itemId) {
        return inventoryItemRepository.findById(itemId)
                .map(inventoryItem -> {
                    inventoryItem.setItemName(newInventoryItem.getItemName());
                    inventoryItem.setItemGroup(newInventoryItem.getItemGroup());
                    inventoryItem.setBrand(newInventoryItem.getBrand());
                    inventoryItem.setUnit(newInventoryItem.getUnit());
                    inventoryItem.setDimension(newInventoryItem.getDimension());
                    inventoryItem.setWeight(newInventoryItem.getWeight());
                    inventoryItem.setDescription(newInventoryItem.getDescription());
                    inventoryItem.setQuantity(newInventoryItem.getQuantity());
                    inventoryItem.setStatus(newInventoryItem.getStatus());
                    if (newInventoryItem.getImagePath() != null) {
                        inventoryItem.setImagePath(newInventoryItem.getImagePath());
                    }
                    return inventoryItemRepository.save(inventoryItem);
                })
                .orElseThrow(() -> new InventoryItemNotFoundException(itemId));
    }

    @Override
    public InventoryItem markAsInactive(long itemId) {
        return inventoryItemRepository.findById(itemId)
                .map(inventoryItem -> {
                    inventoryItem.setStatus(StatusEnum.INACTIVE);
                    return inventoryItemRepository.save(inventoryItem);
                })
                .orElseThrow(() -> new InventoryItemNotFoundException(itemId));
    }

    @Override
    public InventoryItem markAsActive(long itemId) {
        return inventoryItemRepository.findById(itemId)
                .map(inventoryItem -> {
                    inventoryItem.setStatus(StatusEnum.ACTIVE);
                    return inventoryItemRepository.save(inventoryItem);
                })
                .orElseThrow(() -> new InventoryItemNotFoundException(itemId));
    }

    @Override
    public String deleteItemById(long itemId) {
        if (!inventoryItemRepository.existsById(itemId)) {
            throw new InventoryItemNotFoundException(itemId);
        }
        if (isItemInUse(itemId)) {
            throw new InventoryItemInUseException(itemId);
        }

        inventoryItemRepository.deleteById(itemId);
        return "Inventory Item with id " + itemId + "deleted successfully";
    }

    private boolean isItemInUse(long itemId) {
        boolean isRequested = inventoryRequestRepository.existsByInventoryItem_ItemId(itemId);
        boolean isReserved = reservationRepository.existsByItemId(itemId);
        boolean isInTickets = ticketRepository.existsByItemId_ItemId(itemId);

        return isRequested || isReserved || isInTickets;
    }


    //@Override
    //public InventoryItem findByItemNameAndBrand(String itemName, String brand) {
    //return inventoryItemRepository.findByItemNameAndBrand(itemName, brand);
    // }


    @Override
    public Boolean isActive(long itemId) {
        InventoryItem item = inventoryItemRepository.findById(itemId)
                .orElseThrow(() -> new InventoryItemNotFoundException(itemId));
        return item.getStatus() == StatusEnum.ACTIVE;

    }

    @Override
    public int getCountOfInventoryItems() {

        return inventoryItemRepository.countInventoryItem();
    }

    @Override
    public int getCountOfLowStock() {

        return inventoryItemRepository.countLowStock();
    }

    @Override
    public List<InventoryItem> getItemByItemName(String itemName, ItemGroupEnum... itemGroup) {
        List<InventoryItem> itemsByName = inventoryItemRepository.findAllByItemNameContainingIgnoreCase(itemName);

        if (itemGroup != null && itemGroup.length > 0) {
            // Filter items by the provided item group
            return itemsByName.stream()
                    .filter(item -> item.getItemGroup().equals(itemGroup[0]))
                    .collect(Collectors.toList());
        }

        return itemsByName;
    }

    public List<LowStockItemDTO> getLowStockItems() {
        return inventoryItemRepository.findLowStockItems();
    }

    public List<InventorySummaryDto> getInventorySummary(ItemGroupEnum itemGroup) {
        List<InventoryItem> items;
        if (itemGroup == ItemGroupEnum.OTHER) {
            items = inventoryItemRepository.findAllItems();
        } else {
            items = inventoryItemRepository.findByItemGroup(itemGroup);
        }

        return items.stream().map(item -> {
            Integer totalStockIn = stockInRepository.findTotalStockIn(item);
            Integer totalStockOut = stockOutRepository.findTotalStockOut(item);
//            Integer availableQuantity = (totalStockIn != null ? totalStockIn : 0) - (totalStockOut != null ? totalStockOut : 0);
            Integer availableQuantity = (int) item.getQuantity();

            return new InventorySummaryDto(item.getItemId(), item.getItemName(), totalStockIn, totalStockOut, availableQuantity);
        }).collect(Collectors.toList());
    }
}

