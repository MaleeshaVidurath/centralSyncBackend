package CentralSync.demo.model;


import CentralSync.demo.util.EmptyStringToNullDeserializer;
import CentralSync.demo.validation.ValidUnit;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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
    //@ValidUnit
    @NotBlank(message = "Unit is required ")
    private String unit;
    @NotBlank(message = "Dimension is required")
    private String dimension;
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$", message = "Weight is required & must contain both value and measure unit")
    private String weight;
    private String description;

    @Positive(message = "Valid quantity is required")
    private long quantity;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

}
