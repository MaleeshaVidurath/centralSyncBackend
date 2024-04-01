package CentralSync.demo.service;

import CentralSync.demo.exception.AdjustmentNotFoundException;
import CentralSync.demo.model.Adjustment;
import CentralSync.demo.repository.AdjustmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

//import java.util.Optional;
@Service
public class AdjustmentServiceImpl implements AdjustmentService {

    @Autowired //ingect the repository
    private AdjustmentRepository adjustmentRepository;

    @Override
    public Adjustment saveAdjustment(Adjustment adjustment){
        return adjustmentRepository.save(adjustment);
    }

    @Override
    public List<Adjustment> getAllAdjustments() {
        return adjustmentRepository.findAll();
    }

    @Override
    public Adjustment getAdjustmentById(long adjId) {
        return adjustmentRepository.findById(adjId)
                .orElseThrow(()-> new AdjustmentNotFoundException(adjId));
    }

    @Override
    public Adjustment updateAdjustmentById(Adjustment newAdjustment, long adjId){
        return adjustmentRepository.findById(adjId)
                .map(adjustment -> {
                    adjustment.setStatus(newAdjustment.getStatus());
                    adjustment.setReason(newAdjustment.getReason());
                    adjustment.setDate(newAdjustment.getDate());
                    adjustment.setDescription(newAdjustment.getDescription());
                    adjustment.setNewQuantity(newAdjustment.getNewQuantity());

                    return adjustmentRepository.save(adjustment);
                }).orElseThrow(()->new AdjustmentNotFoundException(adjId));
    }
    @Override
    public String deleteAdjustmentById(long adjId){
        if(!adjustmentRepository.existsById(adjId)){
            throw new AdjustmentNotFoundException(adjId);
        }
        adjustmentRepository.deleteById(adjId);
        return  "Adjustment with id "+adjId+" has been deleted successfully.";
    }




}
