package CentralSync.demo.service.UserManagementModule;

import CentralSync.demo.model.UserManagementModule.UserActivityLog;

import java.util.List;

public interface UserActivityLogService {
    public UserActivityLog logUserActivity(Long userId, String action);
    public List<UserActivityLog> getAllUserActivities();
}
