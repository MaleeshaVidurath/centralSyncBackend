package CentralSync.demo.util;

import CentralSync.demo.dto.InventoryRequestDTO;
import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.InventoryRequest;
import CentralSync.demo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class InventoryRequestConverter {

    private static final Logger logger = LoggerFactory.getLogger(InventoryRequestConverter.class);

    public InventoryRequest toEntity(InventoryRequestDTO dto, User user, InventoryItem inventoryItem) throws IOException {
        logger.info("Converting InventoryRequestDTO to InventoryRequest entity");

        InventoryRequest inventoryRequest = new InventoryRequest();
        inventoryRequest.setQuantity(dto.getQuantity());
        inventoryRequest.setReason(dto.getReason());
        inventoryRequest.setDescription(dto.getDescription());
        inventoryRequest.setReqStatus(dto.getReqStatus());
        inventoryRequest.setUser(user);
        inventoryRequest.setInventoryItem(inventoryItem);

        logger.info("Mapped basic fields from DTO to entity");

        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            String fileName = dto.getFile().getOriginalFilename();
            Path filePath = Paths.get("uploads", fileName);
            Path directoryPath = filePath.getParent();

            try {
                // Ensure the directory exists
                if (!Files.exists(directoryPath)) {
                    Files.createDirectories(directoryPath);
                    logger.info("Created directory: {}", directoryPath);
                }

                logger.info("Attempting to copy file to: {}", filePath);

                // Replace existing file if it exists
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                    logger.info("Deleted existing file: {}", filePath);
                }

                Files.copy(dto.getFile().getInputStream(), filePath);
                inventoryRequest.setFilePath(filePath.toString());
                logger.info("File copied successfully to path: {}", filePath);
            } catch (IOException e) {
                logger.error("Error copying file to path: {}", filePath, e);
                throw e;  // Rethrow the exception after logging it
            }
        }

        logger.info("Converted entity: {}", inventoryRequest);
        return inventoryRequest;
    }

    public InventoryRequestDTO toDTO(InventoryRequest inventoryRequest) {
        InventoryRequestDTO dto = new InventoryRequestDTO();
        dto.setReqId(inventoryRequest.getReqId());
        dto.setCreationDateTime(inventoryRequest.getCreatedDateTime());
        dto.setUpdateDateTime(inventoryRequest.getUpdatedDateTime());
        dto.setQuantity(inventoryRequest.getQuantity());
        dto.setReason(inventoryRequest.getReason());
        dto.setDescription(inventoryRequest.getDescription());
        dto.setFilePath(inventoryRequest.getFilePath());
        dto.setReqStatus(inventoryRequest.getReqStatus());

        User user = inventoryRequest.getUser();
        dto.setUserId(user.getUserId());
        dto.setWorkSite(user.getWorkSite());
        dto.setRole(user.getRole());

        InventoryItem inventoryItem = inventoryRequest.getInventoryItem();
        dto.setItemId(inventoryItem.getItemId());

        return dto;
    }
}
