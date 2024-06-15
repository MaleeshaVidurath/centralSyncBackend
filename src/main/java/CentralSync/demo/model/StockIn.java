package CentralSync.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.ManyToOne;
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

    // foreign key
    private long itemId;

    // Define the foreign key relationship
//    @ManyToOne
//    @JoinColumn(name = "itemId", referencedColumnName = "itemId")
//    private InventoryItem inventoryItem;
//    @ManyToOne
//    private InventoryItem item;
}
