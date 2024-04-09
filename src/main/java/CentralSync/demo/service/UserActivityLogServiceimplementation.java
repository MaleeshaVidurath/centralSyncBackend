package CentralSync.demo.service;

import CentralSync.demo.model.User;
import CentralSync.demo.model.UserActivityLog;
import CentralSync.demo.repository.UserActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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
