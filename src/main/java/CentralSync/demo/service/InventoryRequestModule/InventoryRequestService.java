package CentralSync.demo.service.InventoryRequestModule;

import CentralSync.demo.model.InventoryItemModule.ItemGroupEnum;
import CentralSync.demo.model.InventoryRequestModule.InventoryRequest;

import java.util.List;

public interface InventoryRequestService {


    InventoryRequest saveRequest(InventoryRequest request);


    List<InventoryRequest> getAllRequests();


    List<InventoryRequest> getItemsByGroup_Year(ItemGroupEnum itemGroup, String year);


    InventoryRequest getRequestById(long requestId);


    InventoryRequest updateRequestById(InventoryRequest newRequest, long requestId);


    String deleteRequestById(long requestId);


    InventoryRequest updateInReqStatusAccept(long requestId);


    InventoryRequest updateInReqStatusReject(long requestId);


    InventoryRequest updateInReqStatusSendToAdmin(long reqId);


}