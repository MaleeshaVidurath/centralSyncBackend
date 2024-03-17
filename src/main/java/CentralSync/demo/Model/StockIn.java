package CentralSync.demo.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class StockIn {
    @Id
    @GeneratedValue
    private long stockInId;

     private String itemName;

    public long getStockInId() {
        return stockInId;
    }

    public void setStockInId(long stockInId) {
        this.stockInId = stockInId;
    }

    private int quantity;
     private String date;

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
}
