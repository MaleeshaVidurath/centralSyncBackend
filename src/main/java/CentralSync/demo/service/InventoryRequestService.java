package CentralSync.demo.service;

import CentralSync.demo.model.*;

import java.util.List;

public interface InventoryRequestService {
    InventoryRequest saveRequest(InventoryRequest request);

    List<InventoryRequest> getAllRequests();

    List<InventoryRequest> getItemsByGroup_Year(ItemGroupEnum itemGroup, String year);

    InventoryRequest getRequestById(long requestId);

    InventoryRequest updateRequestById(InventoryRequest newRequest, long requestId);

    InventoryRequest updateInReqStatusAccept(long requestId);
    InventoryRequest updateInReqStatusReject(long requestId);
    String deleteRequestById(long requestId);

}