package CentralSync.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
public class StockOut {
    @Id
    @GeneratedValue
    private long soutId;
    private int outQty;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String department;
    private String description;

    @Enumerated(EnumType.STRING)
    private  ItemGroupEnum itemGroup;

    // foreign key
    private long itemId;


    public long getSoutId() {
        return soutId;
    }

    public void setSoutId(long soutId) {
        this.soutId = soutId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getOutQty() {
        return outQty;
    }

    public void setOutQty(int outQty) {
        this.outQty = outQty;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemGroupEnum getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(ItemGroupEnum itemGroup) {
        this.itemGroup = itemGroup;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }
}
