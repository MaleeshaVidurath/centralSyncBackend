package CentralSync.demo.service;

import CentralSync.demo.exception.InventoryItemNotFoundException;
import CentralSync.demo.exception.InventoryRequestNotFoundException;
import CentralSync.demo.model.InventoryRequest;
import CentralSync.demo.model.RoleEnum;
import CentralSync.demo.model.StatusEnum;
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
        newRequest.setReqStatus(StatusEnum.PENDING);
        newRequest.setRole(RoleEnum.EMPLOYEE);
        return requestRepository.save(newRequest);
    }



    @Override
    public List<InventoryRequest> getAllRequests() {
        return requestRepository.findAll();
    }


//    @Override
//    public List<InventoryRequest> getItemsByGroup_Year(ItemGroupEnum itemGroup, String year) {
//
//        // Convert year to int
//        int yearValue = Integer.parseInt(year);
//
//
//        // Set start date to January 1st of the year and end date to December 31st of the year
//        LocalDate startDate = LocalDate.of(yearValue, Month.JANUARY, 1);
//        LocalDate endDate = LocalDate.of(yearValue, Month.DECEMBER, 31);
//
//        List<InventoryRequest> byYear = requestRepository.findAllByDateBetween(startDate, endDate);
//        List<InventoryRequest> byGroup = requestRepository.findAllByItemGroup(itemGroup);
//
//
//        return byGroup.stream()
//                .filter(byGroupItem -> byYear.stream()
//                        .anyMatch(byYearItem -> byYearItem.getReqId() == byGroupItem.getReqId()))
//                .toList();
//
//    }


    //get inventory requests by item ID method
    @Override
    public InventoryRequest getRequestById(long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new InventoryItemNotFoundException(requestId));
    }


    //Edit method
    @Override
    public InventoryRequest updateRequestById(@RequestBody InventoryRequest newRequest, @PathVariable long requestId) {
        return requestRepository.findById(requestId)
                .map(inventoryRequest -> {
                   // inventoryRequest.setItemId(newRequest.getItemId());
                    inventoryRequest.setItemName(newRequest.getItemName());
                    inventoryRequest.setQuantity(newRequest.getQuantity());
                    inventoryRequest.setDate(newRequest.getDate());
                    inventoryRequest.setReason(newRequest.getReason());
                    inventoryRequest.setDescription(newRequest.getDescription());
                  //  inventoryRequest.setEmployeeName(newRequest.getEmployeeName());
                  //  inventoryRequest.setEmployeeID(newRequest.getEmployeeID());
                  //  inventoryRequest.setDepartment(newRequest.getDepartment());
                    inventoryRequest.setReqStatus(newRequest.getReqStatus());
                    return requestRepository.save(newRequest);
                })
                .orElseThrow(() -> new InventoryRequestNotFoundException(requestId));
    }

    //Inventory request patch methods
    //Update request status to accepted
    @Override
    public InventoryRequest updateInReqStatusAccept(long reqId) {
        return requestRepository.findById(reqId)
                .map(inventoryRequest -> {
                    inventoryRequest.setReqStatus(StatusEnum.ACCEPTED);
                    return requestRepository.save(inventoryRequest);
                })
                .orElseThrow(() -> new InventoryRequestNotFoundException(reqId));
    }


    //Update request status to rejected
    @Override
    public InventoryRequest updateInReqStatusReject(long reqId) {
        return requestRepository.findById(reqId)
                .map(inventoryRequest -> {
                    inventoryRequest.setReqStatus(StatusEnum.REJECTED);
                    return requestRepository.save(inventoryRequest);
                })
                .orElseThrow(() -> new InventoryRequestNotFoundException(reqId));
    }

    //Update request status to sent to admin
    @Override
    public InventoryRequest updateInReqStatusSendToAdmin(long reqId) {
        return requestRepository.findById(reqId)
                .map(inventoryRequest -> {
                    inventoryRequest.setReqStatus(StatusEnum.SENT_TO_ADMIN);
                    return requestRepository.save(inventoryRequest);
                })
                .orElseThrow(() -> new InventoryRequestNotFoundException(reqId));
    }


    //Delete method
    @Override
    public String deleteRequestById(long requestID) {
        if (!requestRepository.existsById(requestID)) {
            throw new InventoryRequestNotFoundException(requestID);
        }
        requestRepository.deleteById(requestID);
        return "Request with id " + requestID + " deleted successfully";
    }
}