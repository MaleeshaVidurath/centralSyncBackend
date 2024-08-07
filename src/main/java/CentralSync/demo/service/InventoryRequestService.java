package CentralSync.demo.service;

import CentralSync.demo.dto.InventoryRequestDTO;
import CentralSync.demo.dto.ItemUsageDTO;
import CentralSync.demo.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface InventoryRequestService {

    Optional<InventoryRequest> findById(Long reqId);
    InventoryRequest saveRequest(InventoryRequest request);

    List<InventoryRequestDTO> getRequestsByUserId(Long userId);
    List<InventoryRequestDTO> getAllRequests();

    Map<String,Object> getMostRequestedItem(ItemGroupEnum itemGroup, String  year);



   // List<InventoryRequest> getItemsByGroup_Year(ItemGroupEnum itemGroup, String year);


    InventoryRequest getRequestById(long requestId);

    User getUserById(Long userId);


    InventoryRequest updateInReqStatusReceived(long reqId);

    String deleteRequestById(long requestId);


    //    @Override
    //    public InventoryRequest updateRequestById(InventoryRequest newRequest, long requestId) {
    //        return requestRepository.findById(requestId)
    //
    //                .map(inventoryRequest -> {
    //                    inventoryRequest.setQuantity(newRequest.getQuantity());
    //                    inventoryRequest.setCreationDateTime(newRequest.getCreationDateTime());
    //                    inventoryRequest.setReason(newRequest.getReason());
    //                    inventoryRequest.setDescription(newRequest.getDescription());
    //                    inventoryRequest.setReqStatus(newRequest.getReqStatus());
    //
    //                    // Ensure the InventoryItem exists
    //                    InventoryItem item = itemRepository.findById(newRequest.getInventoryItem().getItemId())
    //                            .orElseThrow(() -> new InventoryItemNotFoundException(newRequest.getInventoryItem().getItemId()));
    //                    inventoryRequest.setInventoryItem(item);
    //
    //                    inventoryRequest.setUpdateDateTime(LocalDateTime.now());
    //
    //                    return requestRepository.save(inventoryRequest);
    //                })
    //                .orElseThrow(() -> new InventoryRequestNotFoundException(requestId));
    //    }
    InventoryRequest updateRequestById(InventoryRequestDTO newRequestDTO, InventoryRequest existingRequest, InventoryItem inventoryItem);

    InventoryRequest updateInReqStatusAccept(long reqId);

    InventoryRequest updateInReqStatusDispatch(long reqId, String email);
    InventoryRequest updateInReqStatusItemWantToReturn(long reqId);




    InventoryRequest updateInReqStatusReject(long requestId);


    InventoryRequest updateInReqStatusSendToAdmin(long reqId);

    List<InventoryRequest> getRequestsByGroupAndYear(ItemGroupEnum itemGroup, String year);

    InventoryRequest updateInReqStatusDeliver(long reqId);

    Long countByStatusAndUserId(StatusEnum reqStatus, User user);

    public List<ItemUsageDTO> getRequestsByItemId(Long itemId);
    //User getUserByInventoryRequestId(Long reqId);
}