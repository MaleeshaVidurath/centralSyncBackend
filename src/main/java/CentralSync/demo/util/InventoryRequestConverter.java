package CentralSync.demo.util;

import CentralSync.demo.dto.InventoryRequestDTO;
import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.InventoryRequest;
import CentralSync.demo.model.User;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class InventoryRequestConverter {

    public InventoryRequest toEntity(InventoryRequestDTO dto, User user, InventoryItem inventoryItem) throws IOException {
        InventoryRequest inventoryRequest = new InventoryRequest();
        inventoryRequest.setQuantity(dto.getQuantity());
        inventoryRequest.setReason(dto.getReason());
        inventoryRequest.setDescription(dto.getDescription());
        inventoryRequest.setReqStatus(dto.getReqStatus());
        inventoryRequest.setUser(user);
        inventoryRequest.setInventoryItem(inventoryItem);


        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            String fileName = dto.getFile().getOriginalFilename();
            Path filePath = Paths.get("file/directory/path", fileName);
            Files.copy(dto.getFile().getInputStream(), filePath);
            inventoryRequest.setFilePath(filePath.toString());
        }

        return inventoryRequest;
    }

    public InventoryRequestDTO toDTO(InventoryRequest inventoryRequest) {
        InventoryRequestDTO dto = new InventoryRequestDTO();
        dto.setReqId(inventoryRequest.getReqId());
        dto.setCreationDateTime(inventoryRequest.getCreationDateTime());
        dto.setUpdateDateTime(inventoryRequest.getUpdateDateTime());
        dto.setQuantity(inventoryRequest.getQuantity());
        dto.setReason(inventoryRequest.getReason());
        dto.setDescription(inventoryRequest.getDescription());
        dto.setFilePath(inventoryRequest.getFilePath());
        dto.setReqStatus(inventoryRequest.getReqStatus());

        User user = inventoryRequest.getUser();
        dto.setUserId(user.getUserId());

        InventoryItem inventoryItem = inventoryRequest.getInventoryItem();
        dto.setItemId(inventoryItem.getItemId());

        return dto;
    }
}
