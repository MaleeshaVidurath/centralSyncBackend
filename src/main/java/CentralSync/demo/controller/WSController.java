package CentralSync.demo.controller;

import CentralSync.demo.dto.Message;
import CentralSync.demo.service.WSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WSController {
    @Autowired
    private WSService service;


    @PostMapping("/send-message")
    public void sendMessage(@RequestBody final Message message) {
        service.notifyFrontend(message.getMessageContent());
    }

    @PostMapping("/send-private-message/{userId}")
    public void sendPrivateMessage(@PathVariable final String userId,
                                   @RequestBody final Message message) {
        service.notifyUser(userId, message.getMessageContent());
    }
}
