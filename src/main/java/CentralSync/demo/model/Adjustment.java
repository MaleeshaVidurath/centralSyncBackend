package CentralSync.demo.model;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;


import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jakarta.persistence.*;
import jdk.jfr.DataAmount;


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
//    private String filePath;

//    adding foreign keys
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId", referencedColumnName = "itemId")
    private InventoryItem inventoryItem ;



//    @ManyToOne
//    private InventoryItem inventoryItem;
//
//    public InventoryItem getInventoryItem() {
//        return inventoryItem;
//    }
//
//    public void setInventoryItem(InventoryItem inventoryItem) {
//        this.inventoryItem = inventoryItem;
//    }

    public Adjustment(){

    }

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
    // getters
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
