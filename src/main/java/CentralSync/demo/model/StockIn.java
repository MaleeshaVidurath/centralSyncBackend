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

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StockIn {
    @Id
    @GeneratedValue
    private long sinId;
    @NotBlank(message = "Location is required")
    private String location;
    @NotNull(message = "Date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @NotNull(message = "InQuantity quantity is required")
    private int inQty;
    @NotBlank(message = "Description is required")
    private String description;
    private String filePath;

    // foreign key
//    private long itemId;

    // Define the foreign key relationship
    @ManyToOne(fetch = FetchType.EAGER)
    @NotBlank(message = "ItemId is required")
    @JoinColumn(name = "itemId")
    private InventoryItem itemId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User userId;
//    @ManyToOne
//    private InventoryItem item;
}
