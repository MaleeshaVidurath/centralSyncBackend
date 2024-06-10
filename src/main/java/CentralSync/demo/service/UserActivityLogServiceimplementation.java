package CentralSync.demo.service;

import CentralSync.demo.model.User;
import CentralSync.demo.model.UserActivityLog;
import CentralSync.demo.repository.UserActivityLogRepository;
import CentralSync.demo.repository.UserRepository;
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
    @Autowired
    UserRepository userRepository;



    @Override
    public UserActivityLog logUserActivity(Long userId, Long entityId, String action) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserActivityLog userActivityLog = new UserActivityLog();
        userActivityLog.setUser(user);
        userActivityLog.setEntityId(entityId);
        userActivityLog.setAction(action);

        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        String formattedTime = currentTime.format(formatter);

        userActivityLog.setTime(formattedTime);
        userActivityLog.setDate(LocalDate.now());

        return userActivityLogRepository.save(userActivityLog);
    }

    @Override
    public List<UserActivityLog> getAllUserActivities() {
        return userActivityLogRepository.findAll();
    }
    @Override
    public List<UserActivityLog> getUserActivitiesByUser(Long userId){
        User user=userRepository.findById(userId)
             .orElseThrow(() -> new RuntimeException("User not found"));
        List<UserActivityLog> userActivities=userActivityLogRepository.findByUser(user);
        return userActivities;
    }



}
