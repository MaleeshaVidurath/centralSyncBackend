package CentralSync.demo.model;


import jakarta.persistence.*;


import java.util.Date;

@Entity
public class StockIn {
    @Id
    @GeneratedValue
    private long sinId;

    private String location;
    private String date;

    @Enumerated(EnumType.STRING)
    private ItemGroupEnum itemGroup;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ItemGroupEnum getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(ItemGroupEnum itemGroup) {
        this.itemGroup = itemGroup;
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

}
