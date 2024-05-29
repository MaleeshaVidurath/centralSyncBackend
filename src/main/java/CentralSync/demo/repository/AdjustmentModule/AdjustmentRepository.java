package CentralSync.demo.repository.AdjustmentModule;

import CentralSync.demo.model.AdjustmentModule.Adjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdjustmentRepository extends JpaRepository<Adjustment, Long> {
}