package CentralSync.demo.service;

import CentralSync.demo.exception.InventoryItemNotFoundException;
import CentralSync.demo.exception.TicketNotFoundException;
import CentralSync.demo.exception.UserNotFoundException;
import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.Ticket;
import CentralSync.demo.model.TicketStatus;
import CentralSync.demo.repository.InventoryItemRepository;
import CentralSync.demo.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketServiceImplementation implements TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private InventoryItemRepository inventoryItemRepository;
    @Override
    public Ticket saveTicket(Ticket ticket) {
        String itemName = ticket.getItemName();
        String brand = ticket.getBrand();
        InventoryItem inventoryItem = inventoryItemRepository.findByItemNameAndBrand(itemName, brand);
        if (inventoryItem != null) {
            // Set the InventoryItem to the Ticket
            ticket.setItemId(inventoryItem);
            // Save the Ticket with the associated InventoryItem
            return ticketRepository.save(ticket);
        } else {
            // If InventoryItem is not found
            throw new RuntimeException("Inventory item not found for name: " + itemName + " and brand: " + brand);
        }
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
    @Override
    public Optional<Ticket> findById(Long id) {
        return ticketRepository.findById(id);
    }
    @Override
    public Ticket updateTicket(Long id, Ticket newTicket) {
        return ticketRepository.findById(id)
                .map(ticket -> {
                    ticket.setTopic(newTicket.getTopic());
                    ticket.setDescription(newTicket.getDescription());
                    ticket.setDate(newTicket.getDate());
                    ticket.setItemId(newTicket.getItemId());

                    return ticketRepository.save(ticket);
                })
                .orElseThrow(() -> new TicketNotFoundException(id));
    }
    @Override
    public Ticket updateTicketStatusReviewed(long TicketId) {
        return ticketRepository.findById(TicketId)
                .map(ticket -> {
                    ticket.setTicketStatus(TicketStatus.REVIEWED);
                    return ticketRepository.save(ticket);
                })
                .orElseThrow(() -> new UserNotFoundException(TicketId));
    }
    @Override
    public Ticket updateTicketStatusSENDTOADMIN(long TicketId) {
        return ticketRepository.findById(TicketId)
                .map(ticket -> {
                    ticket.setTicketStatus(TicketStatus.SENT_TO_ADMIN);
                    return ticketRepository.save(ticket);
                })
                .orElseThrow(() -> new UserNotFoundException(TicketId));
    }
    @Override
    public Ticket updateTicketStatusPENDING(long TicketId) {
        return ticketRepository.findById(TicketId)
                .map(ticket -> {
                    ticket.setTicketStatus(TicketStatus.PENDING);
                    return ticketRepository.save(ticket);
                })
                .orElseThrow(() -> new UserNotFoundException(TicketId));
    }
    @Override
    public String deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new TicketNotFoundException(id);
        }
        ticketRepository.deleteById(id);
        return "Ticket with id " + id + " has been deleted successfully";
    }
    @Override
    public List<Ticket> getFrequentlyMaintainedItem(ItemGroupEnum itemGroup, String year) {
        //filter by item group and year
        int yearInt = Integer.parseInt(year);
        List<Ticket> byYear = ticketRepository.ticketsByYear(yearInt);
        List<Ticket> byGroup=ticketRepository.findAllByItemId_ItemGroup(itemGroup);

        List<Ticket> filteredTicketsList = byGroup.stream()
                .filter(byGroupItem -> byYear.stream()
                        .anyMatch(byYearItem -> byYearItem.getTicketId().equals(byGroupItem.getTicketId())))
                .toList();

        Map<InventoryItem, Long> itemCountMap = filteredTicketsList.stream()
                .collect(Collectors.groupingBy(Ticket::getItemId, Collectors.counting()));
        InventoryItem maxCountItemId = itemCountMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        return filteredTicketsList.stream()
                .filter(ticket -> Objects.equals(ticket.getItemId(), maxCountItemId))
                .collect(Collectors.toList());

    }

    @Override
    public List<Ticket> getTicketsByItemId(long itemId) {
        InventoryItem item=inventoryItemRepository.findById(itemId)
                . orElseThrow(() -> new InventoryItemNotFoundException(itemId));

        return ticketRepository.findAllByItemId(item);
    }
}




