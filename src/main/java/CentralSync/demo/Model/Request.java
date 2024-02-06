package CentralSync.demo.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Request {
    @Id
    @GeneratedValue
    private Long reqId;
    private String reqStatus;
    private String reqQuantity;
    private String reason;
    private String description;
    private String date;
    private Date depName;


    public Long getReqId() {
        return reqId;
    }

    public void setReqId(Long reqId) {
        this.reqId = reqId;
    }

    public String getReqStatus() {
        return reqStatus;
    }

    public void setReqStatus(String reqStatus) {
        this.reqStatus = reqStatus;
    }

    public String getReqQuantity() {
        return reqQuantity;
    }

    public void setReqQuantity(String reqQuantity) {
        this.reqQuantity = reqQuantity;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Date getDepName() {
        return depName;
    }

    public void setDepName(Date depName) {
        this.depName = depName;
    }





}
