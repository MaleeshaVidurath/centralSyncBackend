package CentralSync.demo.controller.UserManagementModule;

import CentralSync.demo.model.UserManagementModule.UserActivityLog;
import CentralSync.demo.service.UserManagementModule.UserActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-activity-log")

@CrossOrigin(origins = "http://localhost:3000")
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

    @GetMapping("/getAll")
    public List<UserActivityLog> list(){
        return userActivityLogService .getAllUserActivities();
    }


}
