package CentralSync.demo.model;

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

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reqId;

    @Setter
    @Getter
    @NotBlank(message = "Item name is required")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z\\s]*$", message = "Item name is required & must contain only letters")
    private String itemName;

    @Setter
    @Getter
    @NotBlank(message = "Quantity is required")
    private String quantity;

    @Setter
    @Getter
    @Column(updatable = false)
    private LocalDate date;

    @Setter
    @Getter
    @NotBlank(message = "Reason is required")
    private String reason;
    private String description;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "employeeName")
//    @NotBlank(message = "Employee name is required")
//    private User employeeName;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    private StatusEnum reqStatus;


    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Setter
    @Getter
    private String filePath;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn
//    @Enumerated(EnumType.STRING)
//    private InventoryItem itemGroup;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn
//    @Transient
//    private InventoryItem itemId;
//
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "employeeId")
//    @Transient
//    private User employeeID;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "employeeName")
//    @Transient
//    private User department;

    @PrePersist
    public void prePersist() {
        this.date = LocalDate.now(); // Set current date before persisting
    }


}

