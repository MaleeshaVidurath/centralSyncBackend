package CentralSync.demo.service;

import CentralSync.demo.dto.InventoryRequestDTO;
import CentralSync.demo.exception.InventoryItemNotFoundException;
import CentralSync.demo.exception.InventoryRequestNotFoundException;
import CentralSync.demo.model.*;
import CentralSync.demo.repository.InventoryItemRepository;
import CentralSync.demo.repository.InventoryRequestRepository;
import CentralSync.demo.repository.UserRepository;
import CentralSync.demo.util.InventoryRequestConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryRequestServiceImpl implements InventoryRequestService {

    private final InventoryRequestRepository requestRepository;
    private final InventoryItemRepository itemRepository;
    private final UserRepository userRepository;
    private final InventoryRequestConverter converter;

    @Autowired
    public InventoryRequestServiceImpl(InventoryRequestRepository requestRepository, InventoryItemRepository itemRepository, UserRepository userRepository, InventoryRequestConverter converter) {
        this.requestRepository = requestRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.converter = converter;
    }

    @Override
    public InventoryRequest saveRequest(InventoryRequest newRequest) {
        if (newRequest.getReqStatus() == null) {
            newRequest.setReqStatus(StatusEnum.PENDING);
        }

        // Ensure the InventoryItem exists
        InventoryItem item = itemRepository.findById(newRequest.getInventoryItem().getItemId())
                .orElseThrow(() -> new InventoryItemNotFoundException(newRequest.getInventoryItem().getItemId()));
        newRequest.setInventoryItem(item);

        newRequest.setUpdateDateTime(LocalDateTime.now());

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
    public List<InventoryRequestDTO> getAllRequests() {
        List<InventoryRequest> requests = requestRepository.findAll();
        return requests.stream()
                .map(converter::toDTO)
                .collect(Collectors.toList());
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
                    inventoryRequest.setQuantity(newRequest.getQuantity());
                    inventoryRequest.setCreationDateTime(newRequest.getCreationDateTime());
                    inventoryRequest.setReason(newRequest.getReason());
                    inventoryRequest.setDescription(newRequest.getDescription());
                    inventoryRequest.setReqStatus(newRequest.getReqStatus());

                    // Ensure the InventoryItem exists
                    InventoryItem item = itemRepository.findById(newRequest.getInventoryItem().getItemId())
                            .orElseThrow(() -> new InventoryItemNotFoundException(newRequest.getInventoryItem().getItemId()));
                    inventoryRequest.setInventoryItem(item);

                    inventoryRequest.setUpdateDateTime(LocalDateTime.now());

                    return requestRepository.save(inventoryRequest);
                })
                .orElseThrow(() -> new InventoryRequestNotFoundException(requestId));
    }

    @Override
    public InventoryRequest updateInReqStatusAccept(long requestId) {
        return requestRepository.findById(requestId)
                .map(inventoryRequest -> {
                    inventoryRequest.setReqStatus(StatusEnum.ACCEPTED);
                    inventoryRequest.setUpdateDateTime(LocalDateTime.now());
                    return requestRepository.save(inventoryRequest);
                })
                .orElseThrow(() -> new InventoryRequestNotFoundException(requestId));
    }

    @Override
    public InventoryRequest updateInReqStatusDispatch(long reqId, String email) {
        return requestRepository.findById(reqId)
                .map(inventoryRequest -> {
                    inventoryRequest.setReqStatus(StatusEnum.DISPATCHED);
                    inventoryRequest.setUpdateDateTime(LocalDateTime.now());
                    return requestRepository.save(inventoryRequest);
                })
                .orElseThrow(() -> new InventoryRequestNotFoundException(reqId));
    }

    @Override
    public InventoryRequest updateInReqStatusReject(long reqId) {
        return requestRepository.findById(reqId)
                .map(inventoryRequest -> {
                    inventoryRequest.setReqStatus(StatusEnum.REJECTED);
                    inventoryRequest.setUpdateDateTime(LocalDateTime.now());
                    return requestRepository.save(inventoryRequest);
                })
                .orElseThrow(() -> new InventoryRequestNotFoundException(reqId));
    }

    @Override
    public InventoryRequest updateInReqStatusSendToAdmin(long reqId) {
        return requestRepository.findById(reqId)
                .map(inventoryRequest -> {
                    inventoryRequest.setReqStatus(StatusEnum.SENT_TO_ADMIN);
                    inventoryRequest.setUpdateDateTime(LocalDateTime.now());
                    return requestRepository.save(inventoryRequest);
                })
                .orElseThrow(() -> new InventoryRequestNotFoundException(reqId));
    }

    @Override
    public InventoryRequest updateInReqStatusDeliver(long reqId) {
        return requestRepository.findById(reqId)
                .map(inventoryRequest -> {
                    inventoryRequest.setReqStatus(StatusEnum.DELIVERED);
                    inventoryRequest.setUpdateDateTime(LocalDateTime.now());
                    return requestRepository.save(inventoryRequest);
                })
                .orElseThrow(() -> new InventoryRequestNotFoundException(reqId));
    }
    @Override
    public InventoryRequest updateInReqStatusItemReturned(long reqId) {
        return requestRepository.findById(reqId)
                .map(inventoryRequest -> {
                    inventoryRequest.setReqStatus(StatusEnum.ITEM_RETURNED);
                    inventoryRequest.setUpdateDateTime(LocalDateTime.now());
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
    public List<InventoryRequestDTO> getRequestsByGroupAndYear(ItemGroupEnum itemGroup, String year) {
        // Filter by item group and year
        int yearInt = Integer.parseInt(year);
        List<InventoryRequest> byYear = requestRepository.requestsByYear(yearInt);
        List<InventoryRequest> byGroup = requestRepository.findAllByInventoryItem_ItemGroup(itemGroup);

        // Get the intersection of both lists
        List<InventoryRequest> filteredRequests = byYear.stream()
                .filter(byGroup::contains)
                .collect(Collectors.toList());

        // Convert the filtered requests to DTOs
        return filteredRequests.stream()
                .map(converter::toDTO)
                .collect(Collectors.toList());
    }

}
