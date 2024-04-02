package CentralSync.demo.service;

import CentralSync.demo.model.UserActivityLog;
import CentralSync.demo.repository.UserActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserActivityLogServiceimplementation implements UserActivityLogService {

    private final UserActivityLogRepository userActivityLogRepository;

    @Autowired
    public UserActivityLogServiceimplementation(UserActivityLogRepository userActivityLogRepository) {
        this.userActivityLogRepository = userActivityLogRepository;
    }

    public UserActivityLog logUserActivity(Long userId, String action) {
        UserActivityLog userActivityLog = new UserActivityLog();
        userActivityLog.setUserId(userId);
        userActivityLog.setAction(action);
        userActivityLog.setTimestamp(LocalDateTime.now());
        userActivityLogRepository.save(userActivityLog);
        return userActivityLog;
    }



}
