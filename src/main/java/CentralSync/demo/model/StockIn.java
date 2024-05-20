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
}
