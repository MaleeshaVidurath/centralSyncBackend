package CentralSync.demo.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;
    @NotBlank(message = "Topic is required", groups = {CreateGroup.class, UpdateGroup.class})
    private String topic;
    @NotBlank(message = "Description is required", groups = {CreateGroup.class, UpdateGroup.class})
    private String description;
    @NotNull(message = "Date is required", groups = {CreateGroup.class, UpdateGroup.class})
    private LocalDate date;
    private String note;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "itemId") // This maps to the foreign key in Ticket
    private InventoryItem itemId;
    @NotBlank(message = "Item Name is required", groups = {CreateGroup.class})
    @Transient
    private String itemName;
    @NotBlank(message = "Brand Name is required", groups = {CreateGroup.class})
    @Transient
    private String brand;
    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId") // This maps to the foreign key in Ticket
    private User user;
}
