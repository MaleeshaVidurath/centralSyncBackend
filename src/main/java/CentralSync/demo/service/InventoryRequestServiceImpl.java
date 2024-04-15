package CentralSync.demo.service;

import CentralSync.demo.model.*;
import CentralSync.demo.exception.InventoryItemNotFoundException;
import CentralSync.demo.exception.RequestNotFoundException;
import CentralSync.demo.repository.InventoryRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@Service
public class InventoryRequestServiceImpl implements InventoryRequestService {

    @Autowired
    private InventoryRequestRepository requestRepository;

    @Override

    public InventoryRequest saveRequest(InventoryRequest newRequest) {
    newRequest.setReqStatus(StatusEnum.pending);
    return requestRepository.save(newRequest);

    }

    @Override
    public List<InventoryRequest> getAllRequests() {
        return requestRepository.findAll();
    }


    @Override
    public List<InventoryRequest> getItemsByGroup_Year(ItemGroupEnum itemGroup, String year) {

        List<InventoryRequest> byGroup = requestRepository.findAllByItemGroup(itemGroup);
        List<InventoryRequest> byYear = requestRepository.findAllByDateContains(year);

        return byGroup.stream()
                .filter(byGroupItem -> byYear.stream()
                        .anyMatch(byYearItem -> byYearItem.getReqId() == byGroupItem.getReqId()))
                .toList();


    }


    @Override
    public InventoryRequest getRequestById(long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new InventoryItemNotFoundException(requestId));
    }

    @Override
    public InventoryRequest updateRequestById(@RequestBody InventoryRequest newRequest, @PathVariable long requestId) {
        return requestRepository.findById(requestId)
                .map(inventoryRequest -> {
                    inventoryRequest.setItemId(newRequest.getItemId());
                    inventoryRequest.setItemName(newRequest.getItemName());
                    inventoryRequest.setQuantity(newRequest.getQuantity());
                    inventoryRequest.setDate(newRequest.getDate());
                    inventoryRequest.setReason(newRequest.getReason());
                    inventoryRequest.setDescription(newRequest.getDescription());
                    inventoryRequest.setEmployeeName(newRequest.getEmployeeName());
                    inventoryRequest.setEmployeeID(newRequest.getEmployeeID());
                    inventoryRequest.setDepartment(newRequest.getDepartment());
                    inventoryRequest.setReqStatus(newRequest.getReqStatus());
                    return requestRepository.save(newRequest);
                })
                .orElseThrow(() -> new RequestNotFoundException(requestId));
    }

    public InventoryRequest updateInventoryRequestStatus(long requestId) {
        return requestRepository.findById(requestId)
                .map(inventoryRequest -> {
                    inventoryRequest.setReqStatus(StatusEnum.accepted);
                    return requestRepository.save(inventoryRequest);
                })
                .orElseThrow(()->new InventoryItemNotFoundException(requestId));
    }

    @Override
    public String deleteRequestById(long requestID) {
        if (!requestRepository.existsById(requestID)) {
            throw new RequestNotFoundException(requestID);
        }
        requestRepository.deleteById(requestID);
        return "Request with id " + requestID + " deleted successfully";
    }



}