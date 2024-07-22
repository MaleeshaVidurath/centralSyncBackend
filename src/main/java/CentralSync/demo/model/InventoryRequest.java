package CentralSync.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class InventoryRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reqId;

    @NotBlank(message = "Quantity is required")
    @Pattern(regexp = "^[1-9][0-9]*$", message = "Quantity must be a positive numeric value")
    private String quantity;

    @Column(updatable = false)
    private LocalDateTime createdDateTime;

    @NotBlank(message = "Reason is required")
    private String reason;

    private String description;

    private String filePath;  // This field will store the file path

    @Enumerated(EnumType.STRING)
    private StatusEnum reqStatus;

    private LocalDateTime updatedDateTime; // New field to store update date and time

    @PrePersist
    public void prePersist() {
        this.createdDateTime = LocalDateTime.now(); // Set current date before persisting
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedDateTime = LocalDateTime.now(); // Set current date before updating
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = false)
    @JsonBackReference("user-inventoryRequests")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "itemId", nullable = false)
    @JsonBackReference("inventoryItem-inventoryRequests")
    private InventoryItem inventoryItem;

    private String formattedCreatedDateTime;

}
