package CentralSync.demo.model.StockInStockOutModule;

import CentralSync.demo.model.InventoryItemModule.ItemGroupEnum;
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
    @Enumerated(EnumType.STRING)
    private ItemGroupEnum itemGroup;
    private int inQty;
    private String description;
    private String filePath;

    // foreign key
    private long itemId;

    // Define the foreign key relationship
//    @ManyToOne
//    @JoinColumn(name = "itemId", referencedColumnName = "itemId")
//    private InventoryItem inventoryItem; // Reference to the InventoryItem entity
}
