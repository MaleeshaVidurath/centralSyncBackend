package CentralSync.demo.model;

import jakarta.persistence.*;

@Entity
public class StockOut {
    @Id
    @GeneratedValue
    private long soutId;


    private int outQty;
    private String date;

    @Enumerated(EnumType.STRING)
    private  ItemGroupEnum itemGroup;

    public String getDate() {
        return date;
    }

    public long getStockOutId() {
        return soutId;
    }

    public void setStockOutId(long soutId) {
        this.soutId = soutId;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public int getQuantity() {
        return outQty;
    }

    public void setQuantity(int outQty) {
        this.outQty = outQty;
    }

    public ItemGroupEnum getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(ItemGroupEnum itemGroup) {
        this.itemGroup = itemGroup;
    }
}
