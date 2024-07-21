package CentralSync.demo.dto;
import lombok.Data;

@Data
public class ItemUsageDTO {

        private Long userId;
        private String itemName;
        private int quantity;
        private String userName;
        private String userEmail;
        private String department;

}
