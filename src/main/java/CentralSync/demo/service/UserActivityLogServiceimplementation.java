package CentralSync.demo.service;

import CentralSync.demo.model.UserActivityLog;
import CentralSync.demo.repository.UserActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserActivityLogServiceimplementation implements UserActivityLogService {

    private final UserActivityLogRepository userActivityLogRepository;

    @Autowired
    public UserActivityLogServiceimplementation (UserActivityLogRepository userActivityLogRepository) {
        this.userActivityLogRepository = userActivityLogRepository;
    }

    @Override
    public UserActivityLog saveUserActivityLog(UserActivityLog userActivityLog) {
        return userActivityLogRepository.save(userActivityLog);
    }
}
