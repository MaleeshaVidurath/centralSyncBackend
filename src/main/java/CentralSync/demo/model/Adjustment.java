package CentralSync.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;


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
    @NotBlank(message = "Reason is Required")
    private String reason;
    @NotNull(message = "Date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String description;
    @NotNull(message = "Adjusted quantity is required")
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
