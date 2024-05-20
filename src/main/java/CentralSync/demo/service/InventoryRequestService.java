package CentralSync.demo.service;

import CentralSync.demo.model.*;

import java.util.List;

public interface InventoryRequestService {

    //add inventory Request method
    InventoryRequest saveRequest(InventoryRequest request);


    //getAllRequests method
    List<InventoryRequest> getAllRequests();


    //getItemsByGroup method
    List<InventoryRequest> getItemsByGroup_Year(ItemGroupEnum itemGroup, String year);


    //getItemsByGroup method
    InventoryRequest getRequestById(long requestId);


    //getItemsByGroup method
    InventoryRequest updateRequestById(InventoryRequest newRequest, long requestId);


    //Inventory request status patch methods
    InventoryRequest updateInReqStatusAccept(long requestId);
    InventoryRequest updateInReqStatusReject(long requestId);
    InventoryRequest updateInReqStatusSendToAdmin(long reqId);


    //deleteRequestById method
    String deleteRequestById(long requestId);

}