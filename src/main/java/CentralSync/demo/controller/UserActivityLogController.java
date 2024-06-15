package CentralSync.demo.controller;

import CentralSync.demo.model.InventoryItem;
import CentralSync.demo.model.User;
import CentralSync.demo.model.UserActivityLog;
import CentralSync.demo.service.UserActivityLogService;
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
    public ResponseEntity<UserActivityLog> logUserActivity(@RequestParam Long userId, @RequestParam Long entityId, @RequestParam String action) {
        UserActivityLog savedLog = userActivityLogService.logUserActivity(userId, entityId, action);
        return ResponseEntity.ok(savedLog);
    }
    @GetMapping("/getAll")
    public List<UserActivityLog> list(){
        return userActivityLogService .getAllUserActivities();
    }
    @GetMapping("/{userId}")
    public List<UserActivityLog> getUserActivities(@PathVariable Long userId){
        return userActivityLogService.getUserActivitiesByUser(userId);
    }

}
