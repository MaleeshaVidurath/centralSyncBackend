package CentralSync.demo.controller;


import CentralSync.demo.dto.AuthRequest;
import CentralSync.demo.exception.UserNotFoundException;
import CentralSync.demo.model.User;
import CentralSync.demo.service.EmailSenderService;
import CentralSync.demo.service.JwtService;
import CentralSync.demo.service.UserActivityLogService;
import CentralSync.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
    private UserService
            userService;

   @Autowired
   private JwtService jwtService;
 @Autowired
 private AuthenticationManager authenticationManager;

    @Autowired
    private UserActivityLogService userActivityLogService;
//@PreAuthorize("hasAuthority('Admin')")
    @PostMapping("/add")
    //Method for get validation message
    public ResponseEntity<?> add(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }

        User savedUser = userService.saveUser(user);

        //Send email to the user
        String subject = "Welcome to CentralSync";
        String body = "Dear " + user.getFirstName() + ",\n\n"
                + "Welcome to CentralSync!"+",\n"+"Username="+ user.getFirstName() +"\n"+"Defaultpassword=centralSync123"
                +'\n'+"Please log in to your Account and change the password."+"\n"+"Thankyou!!";
        emailSenderService.sendSimpleEmail(user.getEmail(), subject, body);

        // Log user activity
        userActivityLogService.logUserActivity(savedUser.getUserId(), "User added");

        return ResponseEntity.ok("New user is added");
    }


  // @PreAuthorize("hasAuthority('ROLE_Admin')")
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to CentralSync";
    }


    @GetMapping("/getAll")
    public List<User> list(){
        return userService.getAllUsers();
    }

    @GetMapping("users/{id}")
    User getUserById(@PathVariable Long id) {

        return userService.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

    }

    @PutMapping("/update/{id}")
    public User updateUserById(@RequestBody User newUser, @PathVariable Long id) {
        // Update the user
        User updatedUser = userService.updateUser(id, newUser);

        // Log the user activity for the update
        userActivityLogService.logUserActivity(updatedUser.getUserId(), "User updated");

        return updatedUser;
    }


    @DeleteMapping("/delete/{id}")
    String deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @PostMapping("/authenticate")
 public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
      Authentication authenticate = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
    if (authenticate.isAuthenticated()) {

            return jwtService.generateToken(authRequest.getUsername());
        } else
            throw new UsernameNotFoundException("Invalid user request!");
    }
    }



