package CentralSync.demo.model;


import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
public class InventoryRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reqId;

    @NotNull(message = "Item ID is required")
    private long itemId;

    @NotBlank(message = "Item name is required")
    private String itemName;


    @NotBlank(message = "Quantity is required")
    private String quantity;

@NotNull(message = "Date is required")
    private Date date;

@NotBlank(message = "Reason is required")
    private String reason;
    private String description;

@NotBlank(message = "Employee name is required")
    private String employeeName;

@NotNull(message = "Employee ID is required")
    private long employeeID;

@NotBlank(message = "Department is required")
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public String getEmployeeName() {
        return employeeName;
    }

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

