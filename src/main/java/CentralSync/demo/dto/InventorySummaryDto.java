package CentralSync.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventorySummaryDto {
        private Long itemId;
        private String itemName;
        private Integer totalStockIn;
        private Integer totalStockOut;
        private Integer availableQuantity;

}
