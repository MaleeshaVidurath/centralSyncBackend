package CentralSync.demo.model;

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

}
