package CentralSync.demo.controller;

import CentralSync.demo.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/notify")
    public void notifyUser(@RequestParam String userId, @RequestParam String message) {
        try {
            logger.info("Received notify request for user: {}, message: {}", userId, message);
            notificationService.sendNotification(userId, message);
            logger.info("Notification sent to user: {}", userId);
        } catch (Exception e) {
            logger.error("Error sending notification to user: {}", userId, e);
        }
    }
}
