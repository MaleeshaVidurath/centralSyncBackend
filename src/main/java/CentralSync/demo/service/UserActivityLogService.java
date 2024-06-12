package CentralSync.demo.service;

import CentralSync.demo.model.User;
import CentralSync.demo.model.UserActivityLog;

import java.util.List;

public interface UserActivityLogService {

    //Method to save user Activities

    UserActivityLog logUserActivity(Long userId, Long entityId, String action);

    public List<UserActivityLog> getAllUserActivities();


    List<UserActivityLog> getUserActivitiesByUser(Long userId);
}
