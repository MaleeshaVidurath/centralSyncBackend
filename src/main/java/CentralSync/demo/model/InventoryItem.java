package CentralSync.demo.model;

import CentralSync.demo.util.EmptyStringToNullDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Entity
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long itemId;

    @Pattern(regexp = "^[a-zA-Z][a-zA-Z\\s]*$", message = "Item name is required & must contain only letters")
    private String itemName;
    @NotNull(message = "Item group is required")
    @JsonDeserialize(using = EmptyStringToNullDeserializer.class)
    @Enumerated(EnumType.STRING)
    private ItemGroupEnum itemGroup;
    @NotBlank(message = "Brand is required")
    private String brand;
    @NotBlank(message = "Unit is required ")
    private String unit;
    @NotBlank(message = "Model  is required")
    private String model;
    @NotBlank(message = "Dimension is required")
    private String dimension;
    @NotBlank(message = "Weight is required")
    private String weight;
    @NotBlank(message = "Description is required")
    private String description;
    @Positive(message = "Valid quantity is required")
    private long quantity;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
    private String imagePath;


//    @OneToMany(mappedBy = "inventoryItem", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference("inventoryItem-inventoryRequests")
//    private List<InventoryRequest> inventoryRequests;
}
