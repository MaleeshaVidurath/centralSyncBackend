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
    @Pattern(regexp = "^[0-9]*$", message = "Quantity must be a numeric value")
    private String quantity;

    @NotBlank(message = "Reason is required")
    private String reason;

    private String description;

    // File handling field for multipart file uploads
    private MultipartFile file;

    // New field to store the file path if needed
    private String filePath;

    // Status and role enums
    private StatusEnum reqStatus;
    private RoleEnum role;

    // User information fields

    private long userId;

    // Lombok @Getter and @Setter annotations handle getter and setter generation
}
