package CentralSync.demo.dto;

import CentralSync.demo.model.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventorySummaryDto {
        private Long itemId;
        private String itemName;
        private String itemDetails;
        private StatusEnum status;
        private Integer availableQuantity;

}
