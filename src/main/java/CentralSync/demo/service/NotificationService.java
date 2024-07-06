package CentralSync.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendNotification(String userId, String message) {
        String destination = "/user/" + userId + "/topic/notifications";
        Notification notification = new Notification(message);
        logger.info("Sending notification to user: {}, message: {}", userId, message);
        messagingTemplate.convertAndSendToUser(userId, "/topic/notifications", notification);
        logger.info("Notification sent to user: {}", userId);
    }

    public static class Notification {
        private String message;

        public Notification(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        // Getters and setters
    }
}
