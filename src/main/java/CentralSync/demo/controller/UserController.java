package CentralSync.demo.controller;

import CentralSync.demo.model.*;
import CentralSync.demo.model.UserStatus;
import CentralSync.demo.exception.UserNotFoundException;
import CentralSync.demo.repository.UserRepository;
import CentralSync.demo.service.EmailSenderService;
import CentralSync.demo.service.UserActivityLogService;
import CentralSync.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserActivityLogService userActivityLogService;

    @PostMapping("/add")
    //Method for get validation message
    public ResponseEntity<?> add(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }

        user.setStatus(UserStatus.ACTIVE);
        User savedUser = userService.saveUser(user);

        //Send email to the user
        String subject = "Welcome to CentralSync";
        String body = "Dear " + user.getFirstName() + ",\n\n"
                + "Welcome to CentralSync!" + ",\n" + "Username=" + user.getFirstName() + "\n" + "Defaultpassword=centralSync123"
                + '\n' + "Please log in to your Account and change the password." + "\n" + "Thankyou!!";
        emailSenderService.sendSimpleEmail(user.getEmail(), subject, body);

        // Log user activity
        userActivityLogService.logUserActivity(savedUser.getUserId(), "User added");

        return ResponseEntity.ok("New user is added");
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
    public ResponseEntity<?> updateUserById(@RequestBody User newUser, @PathVariable Long id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }
        // Update the user
        User updatedUser = userService.updateUser(id, newUser);

        // Log the user activity for the update
        userActivityLogService.logUserActivity(updatedUser.getUserId(), "User updated");

        return ResponseEntity.ok(" User is updated");
    }

    @PatchMapping("/updateStatus/{UserId}")
    public User updateStatus(@PathVariable long UserId) {
        return userService.updateUserStatus(UserId);
    }





        @DeleteMapping("/delete/{id}")
        String deleteUser (@PathVariable Long id){

        return userService.deleteUser(id);

    }
}
