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


    public long getSoutId() {
        return soutId;
    }

    public void setSoutId(long soutId) {
        this.soutId = soutId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public int getOutQty() {
        return outQty;
    }

    public void setOutQty(int outQty) {
        this.outQty = outQty;
    }

    public ItemGroupEnum getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(ItemGroupEnum itemGroup) {
        this.itemGroup = itemGroup;
    }
}
