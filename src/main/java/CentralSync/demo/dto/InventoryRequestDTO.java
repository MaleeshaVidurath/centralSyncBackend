package CentralSync.demo.dto;

import CentralSync.demo.model.RoleEnum;
import CentralSync.demo.model.StatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class InventoryRequestDTO {

    @NotBlank(message = "Item name is required")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z\\s]*$", message = "Item name must contain only letters")
    private String itemName;

    @NotBlank(message = "Quantity is required")
    @Pattern(regexp = "^[1-9][0-9]*$", message = "Quantity must be a positive numeric value")
    private String quantity;

    @NotBlank(message = "Reason is required")
    private String reason;

    private String description;

    // Field for handling file uploads
    private MultipartFile file;

    // Field to store the file path, if needed
    private String filePath;

    // Fields for status and role enums
    private StatusEnum reqStatus;
    private RoleEnum role;

    // Fields to store user and item identifiers
    private long userId;
    private long itemId;
}
