package CentralSync.demo.service;

import CentralSync.demo.exception.UserNotFoundException;
import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.Ticket;
import CentralSync.demo.exception.TicketNotFoundException;
import CentralSync.demo.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import CentralSync.demo.repository.InventoryItemRepository;
import CentralSync.demo.model.TicketStatus;

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
                    ticket.setStatus(newTicket.getStatus());
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



