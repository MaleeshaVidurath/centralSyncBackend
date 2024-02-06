package CentralSync.demo.repository;

import CentralSync.demo.Model.Adjustment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdjustmentRepository extends JpaRepository<Adjustment, Long> {
}