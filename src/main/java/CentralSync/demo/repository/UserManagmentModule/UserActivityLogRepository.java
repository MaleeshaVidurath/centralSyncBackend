package CentralSync.demo.repository.UserManagmentModule;

import CentralSync.demo.model.UserManagementModule.UserActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, Long> {

}
