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

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "itemId")
//    private InventoryItem itemId;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "userId")
//    private User userId;

//    foreign keys without specifing in hibernet.
    private long itemId;
    private  long userId;

}
