package CentralSync.demo.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @NotBlank(message = "Topic is required", groups = {CreateGroup.class, UpdateGroup.class})
    private String topic;
    @Lob
    @NotBlank(message = "Description is required", groups = {CreateGroup.class, UpdateGroup.class})

    private String description;
    @NotNull(message = "Date is required", groups = {CreateGroup.class, UpdateGroup.class})
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String note;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "itemId") // This maps to the foreign key in Ticket
    private InventoryItem itemId;
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z\\s]*$", message = "Item name is required & must contain only letters",groups = {CreateGroup.class})
    @Transient
    private String itemName;
    @NotBlank(message = "Model Name is required",groups = {CreateGroup.class})
    @Transient
    private String model;
    @NotBlank(message = "Brand Name is required",groups = {CreateGroup.class})
    @Transient
    private String brand;
    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;
    @Enumerated(EnumType.STRING)
    private TicketStatus previousStatus;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId") // This maps to the foreign key in Ticket
    private User user;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime statusUpdateTime;

    public void setTicketStatus(TicketStatus ticketStatus) {
        if (ticketStatus == TicketStatus.ACCEPTED && this.ticketStatus != TicketStatus.ACCEPTED) {
            this.previousStatus = TicketStatus.ACCEPTED;
        }
        this.ticketStatus = ticketStatus;
        this.statusUpdateTime = LocalDateTime.now();
    }
}
