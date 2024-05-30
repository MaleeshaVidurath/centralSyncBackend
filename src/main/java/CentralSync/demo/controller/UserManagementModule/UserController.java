package CentralSync.demo.controller.UserManagementModule;

import CentralSync.demo.dto.ReqRes;
import CentralSync.demo.exception.UserManagementModule.UserNotFoundException;
import CentralSync.demo.model.UserManagementModule.*;
import CentralSync.demo.service.EmailSenderService;
import CentralSync.demo.service.LoginModule.LoginService;
import CentralSync.demo.service.UserManagementModule.UserActivityLogService;
import CentralSync.demo.service.UserManagementModule.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private LoginService loginService;

    @PostMapping("/auth/add")
    //Method for get validation message
    public ResponseEntity<?> add(@RequestBody @Validated(CreateGroup.class) User user, BindingResult bindingResult) {
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

    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes req){
        return ResponseEntity.ok(loginService.login(req));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes req){
        return ResponseEntity.ok(loginService.refreshToken(req));
    }

   /* @GetMapping("/adminuser/get-profile")
    public ResponseEntity<ReqRes> getMyProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ReqRes response = userManagementService.getMyInfo(email);
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }*/



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
    public ResponseEntity<?> updateUserById(@RequestBody @Validated(UpdateGroup.class) User newUser, @PathVariable Long id, BindingResult bindingResult) {
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
    public ResponseEntity<?> updateStatus(@PathVariable long UserId) {

        User status = userService.updateUserStatus(UserId);
        // Log the user activity for the update
        userActivityLogService.logUserActivity(status.getUserId(), "User marked as Inactive");
        return ResponseEntity.ok(" User is updated");

    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(
            @PathVariable Long id,
            @RequestBody
            @Validated(UpdatePasswordGroup.class)
            User user,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            Map<String, List<String>> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.groupingBy(
                            FieldError::getField,
                            Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                    ));
            return ResponseEntity.badRequest().body(errors);
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("New password and confirm password do not match");
        }

        User updatedUser = userService.updatePassword(id, user.getPassword());
        return ResponseEntity.ok(updatedUser);
    }


    @DeleteMapping("/delete/{id}")
    String deleteUser(@PathVariable Long id) {

        return userService.deleteUser(id);

    }

    @GetMapping("/count") // get number of user
    public long getUserCount() {
        return userService.getCountOfUser();
    }
}