package CentralSync.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class StockOut {
    @Id
    @GeneratedValue
    private long soutId;
    private String department;
    private String date;  // type
    private int outQty;
    private String description;

    //getters and setters

    public long getSoutId() {
        return soutId;
    }

    public void setSoutId(long soutId) {
        this.soutId = soutId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
