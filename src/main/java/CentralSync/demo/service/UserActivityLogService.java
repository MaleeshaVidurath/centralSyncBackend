package CentralSync.demo.service;

import CentralSync.demo.model.User;
import CentralSync.demo.model.UserActivityLog;

import java.util.List;
import java.util.Optional;

public interface UserActivityLogService {
    public UserActivityLog logUserActivity(Long userId, String action);
    public List<UserActivityLog> getAllUserActivities();
}
