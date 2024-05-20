package CentralSync.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import jakarta.validation.constraints.*;

import java.time.LocalDate;


@Entity
public class Ordering {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    @Pattern(regexp = "^[a-zA-Z][a-zA-Z\\s]*$", message = "Vendor name is required & must contain only letters")
    private String vendorName;
    @NotBlank(message = "Email address is required")
    @Email(message = "Invalid email address")
    private String vendorEmail;
    @NotBlank(message = "Company name is required")
    private String companyName;
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Item name is required & must contain only letters")
    private String itemName;
    @NotBlank(message = "Brand name is required")
    private String brandName;
    @Positive(message = "Valid quantity is required")
    private int quantity;
    @Pattern(regexp = "^\\d{10}+$", message = "Mobile number must be 10 digits")
    private String mobile;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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
