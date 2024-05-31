package CentralSync.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
@Entity
public class InventoryRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reqId;

    @Pattern(regexp = "\\d+", message = "Valid Item Id is required (only numbers allowed)")
    private String itemId;

    @NotBlank(message = "Item name is required")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z\\s]*$", message = "Item name is required & must contain only letters")
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

}

