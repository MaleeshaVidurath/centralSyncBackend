package CentralSync.demo.model;

<<<<<<< HEAD
import jakarta.persistence.*;

import java.util.Date;


@Entity
public class StockIn {

    @Id
    @GeneratedValue
    private long stockInId;

    private String itemName;
    private int quantity;
    private String date;
    private String itemGroup;

    public long getStockInId() {
        return stockInId;
    }

    public void setStockInId(long stockInId) {
        this.stockInId = stockInId;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

=======
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class StockIn {
    @Id
    @GeneratedValue
    private long sinId;
    private String location;
    private Date date;
    private int inQty;
    private String description;

    //add getters and setters

    public long getSinId() {
        return sinId;
    }

    public void setSinId(long sinId) {
        this.sinId = sinId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getInQty() {
        return inQty;
    }

    public void setInQty(int inQty) {
        this.inQty = inQty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
>>>>>>> origin
}
