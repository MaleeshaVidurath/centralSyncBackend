package CentralSync.demo.service;

import CentralSync.demo.exception.InventoryItemNotFoundException;
import CentralSync.demo.exception.InventoryRequestNotFoundException;
import CentralSync.demo.model.*;
import CentralSync.demo.repository.InventoryItemRepository;
import CentralSync.demo.repository.InventoryRequestRepository;
import CentralSync.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryRequestServiceImpl implements InventoryRequestService {

    private final InventoryRequestRepository requestRepository;
    private final InventoryItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public InventoryRequestServiceImpl(InventoryRequestRepository requestRepository, InventoryItemRepository itemRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public InventoryRequest saveRequest(InventoryRequest newRequest) {
        if (newRequest.getReqStatus() == null) {
            newRequest.setReqStatus(StatusEnum.PENDING);
        }
        if (newRequest.getRole() == null) {
            newRequest.setRole(RoleEnum.EMPLOYEE);
        }

        // Ensure the InventoryItem exists
        InventoryItem item = itemRepository.findById(newRequest.getInventoryItem().getItemId())
                .orElseThrow(() -> new InventoryItemNotFoundException(newRequest.getInventoryItem().getItemId()));
        newRequest.setInventoryItem(item);

        return requestRepository.save(newRequest);
    }

    @Override
    public List<InventoryRequest> getRequestsByUserId(Long userId) {
        return requestRepository.findByUserUserId(userId);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new InventoryItemNotFoundException(userId));
    }

    @Override
    public List<InventoryRequest> getAllRequests() {
        return requestRepository.findAll();
    }

    @Override
    public InventoryRequest getRequestById(long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new InventoryRequestNotFoundException(requestId));
    }

    @Override
    public InventoryRequest updateRequestById(InventoryRequest newRequest, long requestId) {
        return requestRepository.findById(requestId)
                .map(inventoryRequest -> {
                    inventoryRequest.setItemName(newRequest.getItemName());
                    inventoryRequest.setQuantity(newRequest.getQuantity());
                    inventoryRequest.setDateTime(newRequest.getDateTime());
                    inventoryRequest.setReason(newRequest.getReason());
                    inventoryRequest.setDescription(newRequest.getDescription());
                    inventoryRequest.setReqStatus(newRequest.getReqStatus());

                    // Ensure the InventoryItem exists
                    InventoryItem item = itemRepository.findById(newRequest.getInventoryItem().getItemId())
                            .orElseThrow(() -> new InventoryItemNotFoundException(newRequest.getInventoryItem().getItemId()));
                    inventoryRequest.setInventoryItem(item);

                    return requestRepository.save(inventoryRequest);
                })
                .orElseThrow(() -> new InventoryRequestNotFoundException(requestId));
    }

    @Override
    public InventoryRequest updateInReqStatusAccept(long reqId) {
        return requestRepository.findById(reqId)
                .map(inventoryRequest -> {
                    inventoryRequest.setReqStatus(StatusEnum.ACCEPTED);
                    return requestRepository.save(inventoryRequest);
                })
                .orElseThrow(() -> new InventoryRequestNotFoundException(reqId));
    }

    @Override
    public InventoryRequest updateInReqStatusReject(long reqId) {
        return requestRepository.findById(reqId)
                .map(inventoryRequest -> {
                    inventoryRequest.setReqStatus(StatusEnum.REJECTED);
                    return requestRepository.save(inventoryRequest);
                })
                .orElseThrow(() -> new InventoryRequestNotFoundException(reqId));
    }

    @Override
    public InventoryRequest updateInReqStatusSendToAdmin(long reqId) {
        return requestRepository.findById(reqId)
                .map(inventoryRequest -> {
                    inventoryRequest.setReqStatus(StatusEnum.SENT_TO_ADMIN);
                    return requestRepository.save(inventoryRequest);
                })
                .orElseThrow(() -> new InventoryRequestNotFoundException(reqId));
    }

    @Override
    public String deleteRequestById(long requestId) {
        if (!requestRepository.existsById(requestId)) {
            throw new InventoryRequestNotFoundException(requestId);
        }
        requestRepository.deleteById(requestId);
        return "Request with id " + requestId + " deleted successfully";
    }

    @Override
    public List<InventoryRequest> getRequestsByGroupAndYear(ItemGroupEnum itemGroup, String year) {
        //filter by item group and year
        int yearInt = Integer.parseInt(year);
        List<InventoryRequest> byYear = requestRepository.requestsByYear(yearInt);
        List<InventoryRequest> byGroup=requestRepository.findAllByInventoryItem_ItemGroup(itemGroup);

        return byYear.stream()
                .filter(byGroup::contains)
                .collect(Collectors.toList());


    }
}
