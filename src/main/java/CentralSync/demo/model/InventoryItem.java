package CentralSync.demo.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;


import jakarta.persistence.*;


@Entity
public class InventoryItem {
    @Id
    @GeneratedValue
    private long itemId;

    private String itemName;

    @Enumerated(EnumType.STRING)
    private ItemGroupEnum itemGroup;

    private String brand;
    private String unit;
    private String dimension;
    private String weight;
    private String description;
    private String quantity;


    //foreign key reference
//    @OneToMany(mappedBy = "inventoryItem")
//    private List<Adjustment> adjustments;

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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


    public void setUnit(String unit) {
        this.unit = unit;
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
