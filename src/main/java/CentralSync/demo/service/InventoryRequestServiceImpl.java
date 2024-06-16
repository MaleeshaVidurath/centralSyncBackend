package CentralSync.demo.service;

import CentralSync.demo.exception.InventoryItemNotFoundException;
import CentralSync.demo.exception.InventoryRequestNotFoundException;
import CentralSync.demo.model.InventoryRequest;
import CentralSync.demo.model.StatusEnum;
import CentralSync.demo.model.User;
import CentralSync.demo.repository.InventoryRequestRepository;
import CentralSync.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class InventoryRequestServiceImpl implements InventoryRequestService {

    @Autowired
    private InventoryRequestRepository requestRepository;

    private final UserRepository userRepository;

    @Autowired
    public InventoryRequestServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override

    public InventoryRequest saveRequest(InventoryRequest newRequest) {
        newRequest.setReqStatus(StatusEnum.PENDING);

        newRequest.setRole(RoleEnum.EMPLOYEE);

        return requestRepository.save(newRequest);

    public List<InventoryRequest> getRequestsByUserId(Long userId) {
        return requestRepository.findByUserUserId(userId);

    }
    

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public InventoryRequest saveRequest(InventoryRequest newRequest) {
        if (newRequest.getReqStatus() == null) {
            newRequest.setReqStatus(StatusEnum.PENDING);
        }
        return requestRepository.save(newRequest);
    }

    @Override
    public List<InventoryRequest> getAllRequests() {
        return requestRepository.findAll();
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
                    inventoryRequest.setItemName(newRequest.getItemName());
                    inventoryRequest.setQuantity(newRequest.getQuantity());
                    inventoryRequest.setDateTime(newRequest.getDateTime());
                    inventoryRequest.setReason(newRequest.getReason());
                    inventoryRequest.setDescription(newRequest.getDescription());
                    inventoryRequest.setReqStatus(newRequest.getReqStatus());
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
    public String deleteRequestById(long requestID) {
        if (!requestRepository.existsById(requestID)) {
            throw new InventoryRequestNotFoundException(requestID);
        }
        requestRepository.deleteById(requestID);
        return "Request with id " + requestID + " deleted successfully";
    }
}
