package CentralSync.demo.service.UserManagementModule;

import CentralSync.demo.model.UserManagementModule.UserActivityLog;
import CentralSync.demo.repository.UserManagmentModule.UserActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UserActivityLogServiceimplementation implements UserActivityLogService {

    @Autowired
    UserActivityLogRepository userActivityLogRepository;

    @Override
    //Method to save user Activities
    public UserActivityLog logUserActivity(Long userId, String action) {
        UserActivityLog userActivityLog = new UserActivityLog();
        userActivityLog.setUserId(userId);
        userActivityLog.setAction(action);

        LocalTime currentTime=LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        String formattedTime = currentTime.format(formatter);

        // Set formatted time in the user activity log
        userActivityLog.setTime(formattedTime);
        userActivityLog.setDate(LocalDate.now());
        userActivityLogRepository.save(userActivityLog);
        return userActivityLog;
    }

    @Override
    public List<UserActivityLog> getAllUserActivities() {
        return userActivityLogRepository.findAll();
    }



}
