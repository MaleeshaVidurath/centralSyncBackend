package CentralSync.demo.util;

import CentralSync.demo.dto.InventoryRequestDTO;
import CentralSync.demo.model.InventoryRequest;
import CentralSync.demo.model.User;

import java.time.LocalDateTime;

public class InventoryRequestConverter {
    public static InventoryRequest toEntity(InventoryRequestDTO dto, User user) {
        InventoryRequest inventoryRequest = new InventoryRequest();
        inventoryRequest.setItemName(dto.getItemName());
        inventoryRequest.setQuantity(dto.getQuantity());
        inventoryRequest.setReason(dto.getReason());
        inventoryRequest.setDescription(dto.getDescription());
        inventoryRequest.setRole(dto.getRole());
        inventoryRequest.setUser(user);
        inventoryRequest.setDateTime(LocalDateTime.now());
        return inventoryRequest;
    }

}
