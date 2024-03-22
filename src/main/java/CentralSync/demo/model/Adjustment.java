package CentralSync.demo.model;

import jakarta.persistence.*;

//import java.util.Date;
@Entity
public class Adjustment {
    @Id
    @GeneratedValue
    private long adjId;
    private String status = "pending";
    private String reason;
    private String date;   // data type of date ??
    private String description;
    private int newQuantity;

    //adding foreign keys
    @ManyToOne
    @JoinColumn(name = "item_id")
    private InventoryItem inventoryItem;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public InventoryItem getInventoryItem() {
        return inventoryItem;
    }

    public void setInventoryItem(InventoryItem inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    // getters
    public String getAdjStatus() {
        return status;
    }
    public long getAdjId() {
        return adjId;
    }

    public String getReason() {
        return reason;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public int getNewQuantity() {
        return newQuantity;
    }

    // setters
    public void setAdjStatus(String adjStatus) {
        this.status = adjStatus;
    }
    public void setAdjId(long adjId) {
        this.adjId = adjId;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNewQuantity(int newQuantity) {
        this.newQuantity = newQuantity;
    }
}
