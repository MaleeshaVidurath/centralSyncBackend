package CentralSync.demo.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;
    @NotBlank(message = "Topic is required")
    private String topic;
    @NotBlank(message = "Description is required")
    private String description;
    @NotNull(message = "Date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String note;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "itemId") // This maps to the foreign key in Ticket
    private InventoryItem itemId;
    @NotBlank(message = "Item Name is required")
    @Transient
    private String itemName;
    @NotBlank(message = "Model Name is required")
    @Transient
    private String model;
    @NotBlank(message = "Brand Name is required")
    @Transient
    private String brand;
    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId") // This maps to the foreign key in Ticket
    private User user;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime statusUpdateTime;

    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
        this.statusUpdateTime = LocalDateTime.now();
    }
}
