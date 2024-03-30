package CentralSync.demo.service;

import CentralSync.demo.model.InventoryRequest;

import java.util.List;

public interface InventoryRequestService {
    public InventoryRequest saveRequest(InventoryRequest request);

    public List<InventoryRequest> getAllRequests();

    public InventoryRequest getRequestById(long requestId);

    public InventoryRequest updateRequestById(InventoryRequest newRequest, long requestId);

    public String deleteRequestById(long requestId);

}