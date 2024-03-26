package CentralSync.demo.controller;

import CentralSync.demo.model.User;
import CentralSync.demo.exception.UserNotFoundException;
import CentralSync.demo.repository.UserRepository;
import CentralSync.demo.service.EmailSenderService;
import CentralSync.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired

    private UserService userService;

    @PostMapping("/add")
    public String add(@RequestBody User user) {
        userService.saveUser(user);
        //Send email to the user
        String subject = "Welcome to CentralSync";
        String body = "Dear " + user.getFirstName() + ",\n\n"
                + "Welcome to CentralSync!";

        emailSenderService.sendSimpleEmail(user.getEmail(), subject, body);

        return "New user is added";
    }

    @GetMapping("/getAll")
    public List<User> list() {
        return userService.getAllUsers();
    }

    @GetMapping("users/{id}")
    User getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @PutMapping("/update/{id}")
    public User updateUserById(@RequestBody User newUser, @PathVariable Long id) {
        return userService.updateUser(id, newUser);
    }

    @DeleteMapping("/delete/{id}")
    String deleteUser(@PathVariable Long id){
    return userService.deleteUser(id);
    }
}


