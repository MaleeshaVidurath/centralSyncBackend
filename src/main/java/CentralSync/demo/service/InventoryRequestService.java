package CentralSync.demo.service;

import CentralSync.demo.dto.InventoryRequestDTO;
import CentralSync.demo.model.InventoryRequest;
import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.User;

import java.util.List;

public interface InventoryRequestService {


    InventoryRequest saveRequest(InventoryRequest request);

    List<InventoryRequest> getRequestsByUserId(Long userId);
    List<InventoryRequestDTO> getAllRequests();




   // List<InventoryRequest> getItemsByGroup_Year(ItemGroupEnum itemGroup, String year);


    InventoryRequest getRequestById(long requestId);

    User getUserById(Long userId);


    InventoryRequest updateRequestById(InventoryRequest newRequest, long requestId);


    String deleteRequestById(long requestId);


    InventoryRequest updateInReqStatusAccept(long requestId);


    InventoryRequest updateInReqStatusReject(long requestId);


    InventoryRequest updateInReqStatusSendToAdmin(long reqId);

    List<InventoryRequestDTO> getRequestsByGroupAndYear(ItemGroupEnum itemGroup, String year);

    InventoryRequest updateInReqStatusDeliver(long reqId);


    //User getUserByInventoryRequestId(Long reqId);
}