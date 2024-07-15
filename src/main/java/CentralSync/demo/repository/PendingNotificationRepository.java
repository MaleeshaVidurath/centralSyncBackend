package CentralSync.demo.repository;

import CentralSync.demo.model.PendingNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PendingNotificationRepository extends JpaRepository<PendingNotification, Long> {
    List<PendingNotification> findByUserIdAndSentFalse(String userId);
}
