package CentralSync.demo.model;

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

    // foriegn key should be added as itemId
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
}
