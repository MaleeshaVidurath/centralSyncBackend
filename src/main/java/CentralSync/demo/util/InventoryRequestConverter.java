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
            inventoryRequest.setItemName(dto.getItemName());
            inventoryRequest.setQuantity(dto.getQuantity());
            inventoryRequest.setReason(dto.getReason());
            inventoryRequest.setDescription(dto.getDescription());
            inventoryRequest.setRole(dto.getRole());
            inventoryRequest.setReqStatus(dto.getReqStatus());
            inventoryRequest.setUser(user);
            inventoryRequest.setInventoryItem(inventoryItem);

            return inventoryRequest;
        }
    }



