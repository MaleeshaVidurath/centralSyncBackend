package CentralSync.demo.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class StockOut {
    @Id
    @GeneratedValue
    private long stockOutId;

    private String itemName;
    private int quantity;
    private String date;

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
}
