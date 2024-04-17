package CentralSync.demo.model;

import jakarta.persistence.*;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;

import java.util.Date;


@Entity
public class Ordering {


    @Id
    @GeneratedValue
    private long orderId;

    @NotBlank(message = "Vendor name is required")
    private String vendorName;
    @NotBlank(message = "email address is required")
    @Email(message = "Invalid email address")
    private String vendorEmail;
    @NotBlank(message = "Company name is required")
    private String companyName;
    @NotBlank(message = "Item name is required")
    private String itemName;
    @NotBlank(message = "Brand name is required")
    private String brandName;
    @Positive(message = "Quantity is required")
    private int quantity;
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
    private String mobile;
    @PastOrPresent(message = "Date cannot be in the past")
    private Date date;
    private String description;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long id) {
        this.orderId = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorEmail() {
        return vendorEmail;
    }

    public void setVendorEmail(String vendorEmail) {
        this.vendorEmail = vendorEmail;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

}
