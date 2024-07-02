package CentralSync.demo.service;

import CentralSync.demo.model.Adjustment;
import CentralSync.demo.model.Status;

import java.util.List;

public interface AdjustmentService {
    public Adjustment saveAdjustment(Adjustment adjustment);

    public List<Adjustment> getAllAdjustments();

    public Adjustment getAdjustmentById(Long adjId);

//    List<Adjustment> getAdjustmentsByUserId(Long userId);

    public Adjustment updateAdjustmentById(Adjustment newAdjustment, Long adjId);
    public String deleteAdjustmentById(Long adjId);

    Adjustment updateAdjStatusReject(Long adjId);

    Adjustment updateAdjStatusAccept(Long adjId);

    List<Adjustment> getAdjustmentsByUserId(Long userId);

    Long countByStatusAndUserId(Status status, Long userId);
}
