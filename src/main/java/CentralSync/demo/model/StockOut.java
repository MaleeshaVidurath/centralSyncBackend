package CentralSync.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class StockOut {
    @Id
    @GeneratedValue
    private long stockOutId;

    private String itemName;
    private int quantity;
    private String date;
    private  String itemGroup;

    public String getDate() {
        return date;
    }

    public long getStockOutId() {
        return stockOutId;
    }

    public void setStockOutId(long stockOutId) {
        this.stockOutId = stockOutId;
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
