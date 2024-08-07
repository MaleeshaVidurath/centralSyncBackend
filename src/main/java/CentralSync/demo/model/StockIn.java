package CentralSync.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
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

    private String location;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private int inQty;

    private String description;

    private String filePath;

    // Define the foreign key relationship
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "itemId")
    private InventoryItem itemId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User userId;
}
