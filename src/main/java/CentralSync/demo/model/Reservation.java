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
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resId;
    @NotBlank(message = "Reason is Required")
    private String reason;
    @NotNull(message = "Date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @NotNull(message = "Date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @NotNull(message = "reservation quantity is required")
    private int reservationQuantity;
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
