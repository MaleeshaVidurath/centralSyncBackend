package CentralSync.demo.service;

import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.InventoryRequest;
import CentralSync.demo.model.ItemGroupEnum;
import CentralSync.demo.model.StockIn;

import java.util.List;

public interface InventoryRequestService {
    public InventoryRequest saveRequest(InventoryRequest request);

    public List<InventoryRequest> getAllRequests();

    List<InventoryRequest> getItemsByGroup_Year(ItemGroupEnum itemGroup, String year);

    public InventoryRequest getRequestById(long requestId);

    public InventoryRequest updateRequestById(InventoryRequest newRequest, long requestId);

    public InventoryRequest updateInventoryRequestStatus(long requestId);
    public String deleteRequestById(long requestId);

}