package CentralSync.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class ItemOrder {


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
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Item name is required & must contain only letters")
    private String itemName;
    @NotBlank(message = "Brand name is required")
    private String brandName;
    @Positive(message = "Valid quantity is required")
    private int quantity;
    @Pattern(regexp = "^\\d{10}+$", message = "Mobile number must be 10 digits")
    private String mobile;
    @NotNull(message = "Date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateInitiated;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateCompleted;
    private String description;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String filePath;

  //private String token;


}
