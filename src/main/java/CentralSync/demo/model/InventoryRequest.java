package CentralSync.demo.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;


@Entity
public class InventoryRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reqId;

    @Positive(message = "Valid Item Id is required")
    private long itemId;

    @NotBlank(message = "Item name is required")
    private String itemName;


    @NotBlank(message = "Quantity is required")
    private String quantity;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotBlank(message = "Reason is required")
    private String reason;
    private String description;

    @NotBlank(message = "Employee name is required")
    private String employeeName;

    @Positive(message = "Valid employeeId is required")
    private long employeeID;

    @NotBlank(message = "Department is required")
    private String department;
    @Enumerated(EnumType.STRING)
    private StatusEnum reqStatus;

    @Enumerated(EnumType.STRING)
    private ItemGroupEnum itemGroup;

//reqId getter and setter
    public long getReqId() {
        return reqId;
    }

    public void setReqId(long reqId) {
        this.reqId = reqId;
    }
//itemId getter and setter
    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }
//itemName getter and setter
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
//quantity getter and setter
    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
//date getter and setter
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
//reason getter and setter
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
//description getter and setter
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
//employeeName getter and setter
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
//employeeID getter and setter
    public long getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(long employeeID) {
        this.employeeID = employeeID;
    }
//department getter and setter
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
//itemGroup getter and setter
    public ItemGroupEnum getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(ItemGroupEnum itemGroup) {
        this.itemGroup = itemGroup;
    }
//reqStatus getter and setter
    public StatusEnum getReqStatus() {
        return reqStatus;
    }

    public void setReqStatus(StatusEnum reqStatus) {
        this.reqStatus = reqStatus;
    }

}

