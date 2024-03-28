package CentralSync.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Adjustment {
    @Id
    @GeneratedValue

    private long adjId;
    private String adjStatus;
    private String reason;
    private String date;   // data type of date ??
    private String description;
    private int newQuantity;

    // getters
    public String getAdjStatus() {
        return adjStatus;
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
        this.adjStatus = adjStatus;
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
