package CentralSync.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LowStockItemDTO {
    private Long itemId;
    private String itemName;
    private Long availableQuantity;

}
