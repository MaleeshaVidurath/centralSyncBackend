package CentralSync.demo.controller;

import CentralSync.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "/notify/{userId}", method = RequestMethod.POST)
    public void sendNotification(@RequestBody String message, @PathVariable Long userId) {
        notificationService.notifyUser(userId, message);
    }
}
