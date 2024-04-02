package CentralSync.demo.service;

import CentralSync.demo.model.UserActivityLog;

public interface UserActivityLogService {
    public UserActivityLog logUserActivity(Long userId, String action);
}
