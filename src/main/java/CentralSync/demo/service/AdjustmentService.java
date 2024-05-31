package CentralSync.demo.service;

import CentralSync.demo.model.Adjustment;

import java.util.List;

public interface AdjustmentService {
    public Adjustment saveAdjustment(Adjustment adjustment);

    public List<Adjustment> getAllAdjustments();

    public Adjustment getAdjustmentById(Long adjId);
    public Adjustment updateAdjustmentById(Adjustment newAdjustment, Long adjId);
    public String deleteAdjustmentById(Long adjId);

    Adjustment updateAdjStatusReject(Long adjId);

    Adjustment updateAdjStatusAccept(Long adjId);

}
