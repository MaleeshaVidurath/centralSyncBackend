package CentralSync.demo.service.MaintenanceTicketModule;

import CentralSync.demo.exception.MaintenanceTicketModule.TicketNotFoundException;
import CentralSync.demo.exception.UserManagementModule.UserNotFoundException;
import CentralSync.demo.model.InventoryItemModule.InventoryItem;
import CentralSync.demo.model.MaintenanceTicketModule.Ticket;
import CentralSync.demo.model.MaintenanceTicketModule.TicketStatus;
import CentralSync.demo.repository.InventoryItemModule.InventoryItemRepository;
import CentralSync.demo.repository.TicketModule.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
                    ticket.setTicketStatus(TicketStatus.SEND_TO_ADMIN);
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


}



