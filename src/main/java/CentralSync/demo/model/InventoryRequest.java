package CentralSync.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "Item name is required")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z\\s]*$", message = "Item name is required & must contain only letters")
    private String itemName;

    @NotBlank(message = "Quantity is required")
    @Pattern(regexp = "^[1-9][0-9]*$", message = "Quantity must be a positive numeric value")
    private String quantity;

    @Column(updatable = false)
    private LocalDate date;

    @NotBlank(message = "Reason is required")
    private String reason;

    private String description;

    private String filePath;  // This field will store the file path

    @Enumerated(EnumType.STRING)
    private StatusEnum reqStatus;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @PrePersist
    public void prePersist() {
        this.date = LocalDate.now(); // Set current date before persisting
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = false)
    @JsonBackReference
    private User user;

    // Uncomment and define these relationships as needed
    // @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "employeeName")
    // private User employeeName;

    // @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn
    // private InventoryItem itemGroup;

    // @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn
    // private InventoryItem itemId;

    // @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "employeeId")
    // private User employeeID;

    // @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "employeeName")
    // private User department;
}
