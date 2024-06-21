package CentralSync.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecentlyUsedItemDTO {
    private Long itemId;
    private String itemName;
    private Long totalStockOut;
}
