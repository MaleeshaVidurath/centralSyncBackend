package CentralSync.demo.model.MaintenanceTicketModule;


import CentralSync.demo.model.InventoryItemModule.InventoryItem;
import CentralSync.demo.model.UserManagementModule.CreateGroup;
import CentralSync.demo.model.UserManagementModule.UpdateGroup;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;


@Entity

public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;


    @NotBlank(message = "Topic is required", groups = {CreateGroup.class, UpdateGroup.class})
    private String topic;
    @NotBlank(message = "Description is required", groups = {CreateGroup.class, UpdateGroup.class})
    private String description;
    @NotNull(message = "Date is required", groups = {CreateGroup.class, UpdateGroup.class})
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "itemId") // This maps to the foreign key in Ticket
    private InventoryItem itemId;
    @NotBlank(message = "Item Name is required", groups = {CreateGroup.class})
    @Transient
    private String itemName;
    @NotBlank(message = "Brand Name is reaquired", groups = {CreateGroup.class})
    @Transient
    private String brand;

    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public InventoryItem getItemId() {
        return itemId;
    }

    public void setItemId(InventoryItem itemId) {
        this.itemId = itemId;
    }

    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

}