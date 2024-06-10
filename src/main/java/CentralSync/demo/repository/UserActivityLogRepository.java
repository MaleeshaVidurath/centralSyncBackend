package CentralSync.demo.repository;

import CentralSync.demo.model.User;
import CentralSync.demo.model.UserActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, Long> {

    List<UserActivityLog> findByUser(User user);
}

