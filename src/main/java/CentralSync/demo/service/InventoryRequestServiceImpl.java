package CentralSync.demo.service;

import CentralSync.demo.exception.AdjustmentNotFoundException;
import CentralSync.demo.model.*;
import CentralSync.demo.exception.InventoryItemNotFoundException;
import CentralSync.demo.exception.RequestNotFoundException;
import CentralSync.demo.repository.InventoryRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

        // Convert year to int
        int yearValue = Integer.parseInt(year);

        // Set start date to January 1st of the year and end date to December 31st of the year
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, yearValue);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();

        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        Date endDate = calendar.getTime();

        List<InventoryRequest> byYear = requestRepository.findAllByDateBetween(startDate,endDate);
        List<InventoryRequest> byGroup = requestRepository.findAllByItemGroup(itemGroup);


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

    @Override
    public InventoryRequest updateInReqStatusAccept(long reqId) {
        return requestRepository.findById(reqId)
                .map(inventoryRequest -> {
                    inventoryRequest.setReqStatus(StatusEnum.accepted);
                    return requestRepository.save(inventoryRequest);
                })
                .orElseThrow(()->new RequestNotFoundException(reqId));
    }

    @Override
    public InventoryRequest updateInReqStatusReject(long reqId) {
        return requestRepository.findById(reqId)
                .map(inventoryRequest -> {
                    inventoryRequest.setReqStatus(StatusEnum.rejected);
                    return requestRepository.save(inventoryRequest);
                })
                .orElseThrow(()->new RequestNotFoundException(reqId));
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