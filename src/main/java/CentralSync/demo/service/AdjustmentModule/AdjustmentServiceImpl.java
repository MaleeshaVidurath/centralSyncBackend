package CentralSync.demo.service.AdjustmentModule;

import CentralSync.demo.exception.AdjustmentModule.AdjustmentNotFoundException;
import CentralSync.demo.model.AdjustmentModule.Adjustment;
import CentralSync.demo.model.InventoryItemModule.InventoryItem;
import CentralSync.demo.model.InventoryRequestModule.Status;
import CentralSync.demo.repository.AdjustmentModule.AdjustmentRepository;
import CentralSync.demo.service.InventoryItemModule.InventoryItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdjustmentServiceImpl implements AdjustmentService {

    @Autowired //ingect the repository
    private AdjustmentRepository adjustmentRepository;

    @Autowired
    private InventoryItemService inventoryItemService;

    @Override
    public Adjustment saveAdjustment(Adjustment adjustment){
        return adjustmentRepository.save(adjustment);
    }

    @Override
    public List<Adjustment> getAllAdjustments() {
        return adjustmentRepository.findAll();
    }

    @Override
    public Adjustment getAdjustmentById(Long adjId) {
        return adjustmentRepository.findById(adjId)
                .orElseThrow(()-> new AdjustmentNotFoundException(adjId));
    }

    @Override
    public Adjustment updateAdjustmentById(Adjustment newAdjustment, Long adjId){
        return adjustmentRepository.findById(adjId)
                .map(adjustment -> {
                    adjustment.setStatus(newAdjustment.getStatus());
                    adjustment.setReason(newAdjustment.getReason());
                    adjustment.setDate(newAdjustment.getDate());
                    adjustment.setDescription(newAdjustment.getDescription());
                    adjustment.setAdjustedQuantity(newAdjustment.getAdjustedQuantity());

                    return adjustmentRepository.save(adjustment);
                }).orElseThrow(()->new AdjustmentNotFoundException(adjId));
    }
    @Override
    public String deleteAdjustmentById(Long adjId){
        if(!adjustmentRepository.existsById(adjId)){
            throw new AdjustmentNotFoundException(adjId);
        }
        adjustmentRepository.deleteById(adjId);
        return  "Adjustment with id "+adjId+" has been deleted successfully.";
    }

    @Transactional
    public Adjustment updateAdjStatusAccept(Long adjId) {
        Optional<Adjustment> optionalAdjustment = adjustmentRepository.findById(adjId);
        if (!optionalAdjustment.isPresent()) {
            throw new EntityNotFoundException("Adjustment not found with id: " + adjId);
        }
        Adjustment adjustment = optionalAdjustment.get();
        adjustment.setStatus(Status.ACCEPTED);

        // Fetch the corresponding InventoryItem
        Optional<InventoryItem> optionalInventoryItem = Optional.ofNullable(inventoryItemService.getItemById(adjustment.getItemId()));
        if (!optionalInventoryItem.isPresent()) {
            throw new EntityNotFoundException("Inventory item not found with id: " + adjustment.getItemId());
        }

        InventoryItem inventoryItem = optionalInventoryItem.get();
        inventoryItem.setQuantity(inventoryItem.getQuantity() + adjustment.getAdjustedQuantity());

        // Save the updated InventoryItem
        inventoryItemService.saveItem(inventoryItem);

        // Save the updated Adjustment
        return adjustmentRepository.save(adjustment);
    }
    @Override
    public Adjustment updateAdjStatusReject(Long adjId) {
        return adjustmentRepository.findById(adjId)
                .map(adjustment -> {
                    adjustment.setStatus(Status.REJECTED);
                    return adjustmentRepository.save(adjustment);
                })
                .orElseThrow(()->new AdjustmentNotFoundException(adjId));
    }
}


