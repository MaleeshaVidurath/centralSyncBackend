package CentralSync.demo.model;


import jakarta.persistence.*;

import java.util.Date;


@Entity

public class Ticket  {
    @Id
    @GeneratedValue

    private Long ticketId;
    private String topic;

    private String description;
    private Date date;

    private String status;


    public InventoryItem getItemId() {
        return itemId;
    }

    public void setItemId(InventoryItem itemId) {
        this.itemId = itemId;
    }

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "itemId") // This maps to the foreign key in Ticket
    private InventoryItem itemId;

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

    @Transient
    private String itemName;

    @Transient
    private String brand;
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


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
