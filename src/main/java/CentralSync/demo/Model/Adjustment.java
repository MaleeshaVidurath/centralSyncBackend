package CentralSync.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

//import java.util.Date;
@Entity
public class Adjustment {
    @Id
    @GeneratedValue
    private long adjId;
    private String status = "pending";
    private String reason;
    private String date;   // data type of date ??
    private String description;
    private int newQuantity;

    // getters
    public String getAdjStatus() {
        return status;
    }
    public long getAdjId() {
        return adjId;
    }

    public String getReason() {
        return reason;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public int getNewQuantity() {
        return newQuantity;
    }

    // setters
    public void setAdjStatus(String adjStatus) {
        this.status = adjStatus;
    }
    public void setAdjId(long adjId) {
        this.adjId = adjId;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNewQuantity(int newQuantity) {
        this.newQuantity = newQuantity;
    }
}
