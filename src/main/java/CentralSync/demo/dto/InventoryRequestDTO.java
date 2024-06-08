package CentralSync.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class InventoryRequestDTO {

    @NotBlank(message = "Item name is required")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z\\s]*$", message = "Item name is required & must contain only letters")
    private String itemName;

    @NotBlank(message = "Quantity is required")
    @Pattern(regexp = "^[0-9]*$", message = "Quantity must be a numeric value")
    private String quantity;

    @NotBlank(message = "Reason is required")
    private String reason;

    private String description;

    private MultipartFile file;
}
