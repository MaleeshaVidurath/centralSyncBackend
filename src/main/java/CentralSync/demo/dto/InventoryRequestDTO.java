package CentralSync.demo.dto;

import CentralSync.demo.model.StatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
public class InventoryRequestDTO {

    private long reqId;
    private LocalDateTime creationDateTime; // Renamed from dateTime for clarity

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

    // Fields to store user and item identifiers
    private long userId;
    private long itemId;

    private LocalDateTime updateDateTime; // New field to store update date and time

    private String workSite;

    private String role;

    public  String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public String getWorkSite() {
        return workSite;
    }

    public void setWorkSite(String workSite) {
        this.workSite = workSite;
    }

    public void setReqId(long reqId) {
        this.reqId = reqId;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public void setUpdateDateTime(LocalDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
    }
}
