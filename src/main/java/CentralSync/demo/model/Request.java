package CentralSync.demo.model;

<<<<<<< HEAD
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class InventoryRequest {
=======
import jakarta.persistence.*;

@Entity
public class Request {
>>>>>>> origin

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reqId;
<<<<<<< HEAD
    private long itemId;
    private String Item_Name;
    private String quantity;
    private String date;
=======
    private String reqStatus;
    private String reqQuantity;
>>>>>>> origin
    private String reason;
    private String description;
    private String employeeName;
    private long employeeID;
    private String department;
<<<<<<< HEAD
    private String reqStatus;



    public long getReqId() {
        return reqId;
    }
    public void setReqId(long reqId) {
        this.reqId = reqId;
=======
    @Enumerated(EnumType.STRING)
    private InventoryRequestStatus reqStatus;



    public InventoryRequestStatus getReqStatus() {
        return reqStatus;
    }
    public void setReqStatus(InventoryRequestStatus reqStatus) {
        this.reqStatus = reqStatus;
>>>>>>> origin
    }
    public long getItemId() {
        return itemId;
    }
    public void setItemId(long itemId) {
        this.itemId = itemId;
    }
<<<<<<< HEAD
=======
    public long getReqId() {
        return reqId;
    }
    public void setReqId(long reqId) {
        this.reqId = reqId;
    }
>>>>>>> origin
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
<<<<<<< HEAD
    public String getEmployeeName() {return employeeName;}
=======
    public String getEmployeeName() {
        return employeeName;
    }
>>>>>>> origin
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
<<<<<<< HEAD
    public String getReqStatus() {
        return reqStatus;
    }
    public void setReqStatus(String reqStatus) {
        this.reqStatus = reqStatus;
    }
=======

>>>>>>> origin
}

