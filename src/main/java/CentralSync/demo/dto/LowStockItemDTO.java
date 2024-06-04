package CentralSync.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LowStockItemDTO {
    private long itemId;
    private String itemName;
    private long totalStockIn;
    private long totalStockOut;
    private long availableQuantity;
}
