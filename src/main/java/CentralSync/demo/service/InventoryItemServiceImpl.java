package CentralSync.demo.service;

import CentralSync.demo.dto.LowStockItemDTO;
import CentralSync.demo.exception.InventoryItemInUseException;
import CentralSync.demo.exception.InventoryItemNotFoundException;
import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.StatusEnum;
import CentralSync.demo.repository.InventoryItemRepository;
import CentralSync.demo.repository.InventoryRequestRepository;
import CentralSync.demo.repository.ReservationRepository;
import CentralSync.demo.repository.TicketRepository;
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
        InventoryItem duplicateItem= inventoryItemRepository.findDuplicate( inventoryItem.getItemGroup(), inventoryItem.getBrand(),
                inventoryItem.getModel());
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

    @Override
    public List<String> getModelNamesByItemNameAndBrand(String itemName, String brand) {
        List<InventoryItem> items = inventoryItemRepository.findItemsByItemNameAndBrand(itemName, brand);
        return items.stream()
                .map(InventoryItem::getModel)
                .distinct() // Ensure unique model names
                .collect(Collectors.toList());
    }
}

