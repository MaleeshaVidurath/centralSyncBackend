package CentralSync.demo.model;

import jakarta.persistence.*;

@Entity
public class InventoryRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reqId;
    private long itemId;
    private String Item_Name;
    private String quantity;
    private String date;
    private String reason;
    private String description;
    private String employeeName;
    private long employeeID;
    private String department;
    @Enumerated(EnumType.STRING)
    private StatusEnum reqStatus;
    @Enumerated(EnumType.STRING)
    private ItemGroupEnum itemGroup;


    public long getReqId() {
        return reqId;
    }
    public void setReqId(long reqId) {
        this.reqId = reqId;
    }
    public long getItemId() {
        return itemId;
    }
    public void setItemId(long itemId) {
        this.itemId = itemId;
    }
    public String getItem_Name() {
        return Item_Name;
    }
    public void setItem_Name(String item_Name) {
        Item_Name = item_Name;
    }
    public String getQuantity() {
        return quantity;
    }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getEmployeeName() {return employeeName;}
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    public long getEmployeeID() {
        return employeeID;
    }
    public void setEmployeeID(long employeeID) {
        this.employeeID = employeeID;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public ItemGroupEnum getItemGroup() {return itemGroup;}
    public void setItemGroup(ItemGroupEnum itemGroup) {this.itemGroup = itemGroup;}
    public StatusEnum getReqStatus() {return reqStatus;}
    public void setReqStatus(StatusEnum reqStatus) {this.reqStatus = reqStatus;}
}

