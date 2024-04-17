package CentralSync.demo.repository;

import CentralSync.demo.model.Adjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdjustmentRepository extends JpaRepository<Adjustment, Long> {
}