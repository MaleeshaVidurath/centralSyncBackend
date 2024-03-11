package CentralSync.demo.service;

import CentralSync.demo.model.Adjustment;

import java.util.List;
import java.util.Optional;

public interface AdjustmentService {
    public Adjustment saveAdjustment(Adjustment adjustment);

    public List<Adjustment> getAllAdjustments();

    public Adjustment getAdjustmentById(long adjId);
    public Adjustment updateAdjustmentById(Adjustment newAdjustment, long adjId);
    public String deleteAdjustmentById(long adjId);

}
