package CentralSync.demo.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Adjustment {
    @Id
    @GeneratedValue
    private Long adjId;
    private String reason;
    private String date;   // data type of date ??
    private String description;
    private int newQuantity;

    @Enumerated(EnumType.STRING)
    private Status status;

    //adding foreign keys
//    @ManyToOne
//    @JoinColumn(name = "item_id")
//    private InventoryItem inventoryItem;

    //foreign keys without specifing in hibernet.
    private long itemId;

    private String filePath;
    // Additional field to store file content
    @Lob
    private byte[] fileContent;

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


//    public InventoryItem getInventoryItem() {
//        return inventoryItem;
//    }
//
//    public void setInventoryItem(InventoryItem inventoryItem) {
//        this.inventoryItem = inventoryItem;
//    }

    // getters
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getAdjId() {
        return adjId;
    }

    public void setAdjId(Long adjId) {
        this.adjId = adjId;
    }
}
