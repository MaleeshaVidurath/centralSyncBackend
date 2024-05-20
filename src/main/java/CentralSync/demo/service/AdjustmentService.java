package CentralSync.demo.service;

import CentralSync.demo.model.Adjustment;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface AdjustmentService {
    public Adjustment saveAdjustment(Adjustment adjustment);

    public List<Adjustment> getAllAdjustments();

    public Adjustment getAdjustmentById(Long adjId);
    public Adjustment updateAdjustmentById(Adjustment newAdjustment, Long adjId);
    public String deleteAdjustmentById(Long adjId);

    Adjustment updateAdjStatusReject(Long adjId);

    Adjustment updateAdjStatusAccept(Long adjId);

    int getCountOfAdjustments();
}
