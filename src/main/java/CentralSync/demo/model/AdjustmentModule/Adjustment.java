package CentralSync.demo.model.AdjustmentModule;

import CentralSync.demo.model.InventoryRequestModule.Status;
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
public class Adjustment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adjId;
    private String reason;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String description;
    private int adjustedQuantity;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "file_path")
    private String filePath; // File path to store the uploaded file


    //adding foreign keys
//    @ManyToOne
//    @JoinColumn(name = "item_id")
//    private InventoryItem inventoryItem;


    //foreign keys without specifing in hibernet.
    private long itemId;

}
