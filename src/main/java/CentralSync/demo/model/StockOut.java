package CentralSync.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StockOut {
    @Id
    @GeneratedValue
    private long soutId;
    private int outQty;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String department;
    private String description;
    private String filePath;

    // foreign key
//    private long itemId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "itemId")
    private InventoryItem itemId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User userId;

}
