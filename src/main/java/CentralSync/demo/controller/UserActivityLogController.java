package CentralSync.demo.controller;

import CentralSync.demo.model.User;
import CentralSync.demo.model.UserActivityLog;
import CentralSync.demo.service.UserActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/user-activity-log")


public class UserActivityLogController {
    @Autowired
    private final UserActivityLogService userActivityLogService;


    @Autowired
    public UserActivityLogController(UserActivityLogService userActivityLogService) {
        this.userActivityLogService = userActivityLogService;
    }

    @PostMapping("/log")
    public ResponseEntity<UserActivityLog> logUserActivity(@RequestBody UserActivityLog userActivityLog) {

        UserActivityLog savedLog = userActivityLogService.logUserActivity(userActivityLog.getUserId(), userActivityLog.getAction());
        return ResponseEntity.ok(savedLog);
    }


}
