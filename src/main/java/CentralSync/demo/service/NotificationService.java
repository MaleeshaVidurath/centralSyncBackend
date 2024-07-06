package CentralSync.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifyUser(Long userId, String message) {

        messagingTemplate.convertAndSendToUser(userId.toString(),"/private", message);
        System.out.println("Notification sent to user: " + userId + " with message: " + message);
    }
}
