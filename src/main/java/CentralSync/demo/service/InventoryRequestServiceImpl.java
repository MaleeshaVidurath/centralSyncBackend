package CentralSync.demo.service;

import CentralSync.demo.dto.InventoryRequestDTO;
import CentralSync.demo.dto.ItemUsageDTO;
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
import java.util.*;

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
    public Optional<InventoryRequest> findById(Long reqId) {
        return requestRepository.findById(reqId);
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

        newRequest.setUpdatedDateTime(LocalDateTime.now());

        return requestRepository.save(newRequest);
    }

    @Override
    public List<InventoryRequestDTO> getRequestsByUserId(Long userId) {
        List<InventoryRequest> requests = requestRepository.findByUserUserId(userId);
        return requests.stream()
                .map(converter::toDTO)
                .collect(Collectors.toList());

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
    public InventoryRequest updateRequestById(InventoryRequestDTO newRequestDTO, InventoryRequest existingRequest, InventoryItem inventoryItem) {
        existingRequest.setQuantity(newRequestDTO.getQuantity());
        existingRequest.setReason(newRequestDTO.getReason());
        existingRequest.setDescription(newRequestDTO.getDescription());
        existingRequest.setReqStatus(newRequestDTO.getReqStatus());

        existingRequest.setInventoryItem(inventoryItem);

        return requestRepository.save(existingRequest);
    }


    @Override
    public InventoryRequest updateInReqStatusAccept(long requestId) {
        return requestRepository.findById(requestId)
                .map(inventoryRequest -> {
                    inventoryRequest.setReqStatus(StatusEnum.ACCEPTED);
                    inventoryRequest.setUpdatedDateTime(LocalDateTime.now());
                    return requestRepository.save(inventoryRequest);
                })
                .orElseThrow(() -> new InventoryRequestNotFoundException(requestId));
    }

    @Override
    public InventoryRequest updateInReqStatusDispatch(long reqId, String email) {
        return requestRepository.findById(reqId)
                .map(inventoryRequest -> {
                    inventoryRequest.setReqStatus(StatusEnum.DISPATCHED);
                    inventoryRequest.setUpdatedDateTime(LocalDateTime.now());
                    return requestRepository.save(inventoryRequest);
                })
                .orElseThrow(() -> new InventoryRequestNotFoundException(reqId));
    }

    @Override
    public InventoryRequest updateInReqStatusReject(long reqId) {
        return requestRepository.findById(reqId)
                .map(inventoryRequest -> {
                    inventoryRequest.setReqStatus(StatusEnum.REJECTED);
                    inventoryRequest.setUpdatedDateTime(LocalDateTime.now());
                    return requestRepository.save(inventoryRequest);
                })
                .orElseThrow(() -> new InventoryRequestNotFoundException(reqId));
    }

    @Override
    public InventoryRequest updateInReqStatusSendToAdmin(long reqId) {
        return requestRepository.findById(reqId)
                .map(inventoryRequest -> {
                    inventoryRequest.setReqStatus(StatusEnum.SENT_TO_ADMIN);
                    inventoryRequest.setUpdatedDateTime(LocalDateTime.now());
                    return requestRepository.save(inventoryRequest);
                })
                .orElseThrow(() -> new InventoryRequestNotFoundException(reqId));
    }

    @Override
    public InventoryRequest updateInReqStatusDeliver(long reqId) {
        return requestRepository.findById(reqId)
                .map(inventoryRequest -> {
                    inventoryRequest.setReqStatus(StatusEnum.DELIVERED);
                    inventoryRequest.setUpdatedDateTime(LocalDateTime.now());
                    return requestRepository.save(inventoryRequest);
                })
                .orElseThrow(() -> new InventoryRequestNotFoundException(reqId));
    }

    @Override
    public InventoryRequest updateInReqStatusItemWantToReturn(long reqId) {
        return requestRepository.findById(reqId)
                .map(inventoryRequest -> {
                    inventoryRequest.setReqStatus(StatusEnum.WANT_TO_RETURN_ITEM);
                    inventoryRequest.setUpdatedDateTime(LocalDateTime.now());
                    return requestRepository.save(inventoryRequest);
                })
                .orElseThrow(() -> new InventoryRequestNotFoundException(reqId));
    }

    @Override
    public InventoryRequest updateInReqStatusReceived(long reqId) {
        return requestRepository.findById(reqId)
                .map(inventoryRequest -> {
                    inventoryRequest.setReqStatus(StatusEnum.RECEIVED);
                    inventoryRequest.setUpdatedDateTime(LocalDateTime.now());
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
        // Filter by item group and year
        int yearInt = Integer.parseInt(year);
        List<InventoryRequest> byYear = requestRepository.requestsByYear(yearInt);
        List<InventoryRequest> byGroup = requestRepository.findAllByInventoryItem_ItemGroup(itemGroup);

        // Get the intersection of both lists
        return byYear.stream()
                .filter(byGroup::contains)
                .collect(Collectors.toList());


    }

    @Override

    public Map<String, Object> getMostRequestedItem(ItemGroupEnum itemGroup, String year) {
        //Filter by itemGroup and year
        int yearInt = Integer.parseInt(year);
        List<InventoryRequest> byYear = requestRepository.requestsByYear(yearInt);
        List<InventoryRequest> byGroup = requestRepository.findAllByInventoryItem_ItemGroup(itemGroup);

        List<InventoryRequest> filteredRequestList = byGroup.stream()
                .filter(byGroupItem -> byYear.stream()
                        .anyMatch(byYearItem -> byYearItem.getInventoryItem().equals(byGroupItem.getInventoryItem())))
                .filter(request->!request.getReqStatus().equals(StatusEnum.WANT_TO_RETURN_ITEM))
                .toList();

        // Count occurrences of each InventoryItem in the filtered list
        Map<InventoryItem, Long> itemCountMap = filteredRequestList.stream()
                .collect(Collectors.groupingBy(InventoryRequest::getInventoryItem, Collectors.counting()));


        // Find the InventoryItem with the maximum count and its count
        Map.Entry<InventoryItem, Long> maxEntry = itemCountMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        if (maxEntry != null) {
            Map<String, Object> result = new HashMap<>();
            result.put("item", maxEntry.getKey());
            result.put("count", maxEntry.getValue());
            return result;
        } else {
            return null;
        }

    }

    public Long countByStatusAndUserId(StatusEnum reqStatus, User user) {
        return requestRepository.countByReqStatusAndUser(reqStatus, user);
    }

    @Override
    public List<ItemUsageDTO> getRequestsByItemId(Long itemId) {
        List<InventoryRequest> requests = requestRepository.findByInventoryItem_ItemId(itemId);
        System.out.println(requests);
        return requests.stream().map(request -> {
            ItemUsageDTO dto = new ItemUsageDTO();

            dto.setItemName(request.getInventoryItem().getItemName());
            dto.setQuantity(Integer.parseInt(request.getQuantity()));
            dto.setUserId(request.getUser().getUserId());
            dto.setUserName(request.getUser().getFirstName() + " " + request.getUser().getLastName());
            dto.setUserEmail(request.getUser().getEmail());
            dto.setDepartment(request.getUser().getDepartment());
            return dto;
        }).collect(Collectors.toList());
    }
}
