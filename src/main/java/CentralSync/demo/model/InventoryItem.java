package CentralSync.demo.model;


import CentralSync.demo.util.EmptyStringToNullDeserializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Entity
public class InventoryItem {
    @Id
    @GeneratedValue
    private long itemId;
    @NotBlank(message = "Item name is required")
    private String itemName;

    @NotNull(message = "Item group is required")
    @JsonDeserialize(using = EmptyStringToNullDeserializer.class)
    @Enumerated(EnumType.STRING )
    private ItemGroupEnum itemGroup;

    @NotBlank(message = "Brand is required")
    private String brand;
    @NotBlank(message = "Unit is required")
    private String unit;
    private String dimension;
    private String weight;
    private String description;
    @NotBlank(message = "Quantity is required")
    private String quantity;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;


    public InventoryItem() {
    }


    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public ItemGroupEnum getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(ItemGroupEnum itemGroup) {
        this.itemGroup = itemGroup;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }
}
