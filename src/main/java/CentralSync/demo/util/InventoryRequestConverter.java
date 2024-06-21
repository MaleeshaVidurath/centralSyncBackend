package CentralSync.demo.util;

import CentralSync.demo.dto.InventoryRequestDTO;
import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.InventoryRequest;
import CentralSync.demo.model.User;
import org.springframework.stereotype.Component;

@Component
public class InventoryRequestConverter {

    public InventoryRequest toEntity(InventoryRequestDTO dto, User user, InventoryItem inventoryItem) {
        InventoryRequest inventoryRequest = new InventoryRequest();
        inventoryRequest.setQuantity(dto.getQuantity());
        inventoryRequest.setReason(dto.getReason());
        inventoryRequest.setDescription(dto.getDescription());
        inventoryRequest.setReqStatus(dto.getReqStatus());
        inventoryRequest.setUser(user);
        inventoryRequest.setInventoryItem(inventoryItem);

        // No need to set creationDateTime and updateDateTime here as they are managed by entity lifecycle callbacks

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
