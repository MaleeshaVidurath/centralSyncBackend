package CentralSync.demo.service;

import CentralSync.demo.model.UserActivityLog;

import java.util.List;

public interface UserActivityLogService {
    public UserActivityLog logUserActivity(Long userId, String action);
    public List<UserActivityLog> getAllUserActivities();
}
