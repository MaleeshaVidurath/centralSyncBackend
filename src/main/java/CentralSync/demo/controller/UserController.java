package CentralSync.demo.controller;

import CentralSync.demo.dto.ReqRes;
import CentralSync.demo.exception.InvalidTokenException;
import CentralSync.demo.exception.UserNotFoundException;
import CentralSync.demo.model.*;
import CentralSync.demo.service.EmailSenderService;
import CentralSync.demo.service.LoginService;
import CentralSync.demo.service.UserActivityLogService;
import CentralSync.demo.service.UserService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private Long userId;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserActivityLogService userActivityLogService;
    @Autowired
    private LoginService loginService;
    @PostMapping("/add")
    //Method for get validation message
    public ResponseEntity<?> add(@RequestPart("user") @Validated(CreateGroup.class) User user, BindingResult bindingResult,@RequestPart(value = "image", required = false) MultipartFile image, Principal principal) throws MessagingException {



        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }

        if (image != null && !image.isEmpty()) {
            try {
                byte[] bytes = image.getBytes();
                Path path = Paths.get("uploads/" + image.getOriginalFilename());
                Files.write(path, bytes);
                user.setImagePath(path.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
            }
        }



        user.setStatus(UserStatus.INACTIVE);
        User savedUser = userService.saveUser(user);
        // Generate and send verification email
        userService.sendRegistrationConfirmationEmail(savedUser);
        // Log user activity
        Long actorId = loginService.userId;
        userActivityLogService.logUserActivity(actorId, savedUser.getUserId(), "New user added");
        return ResponseEntity.ok("New user is added");
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam("token") String token) {
        try {
            User user = userService.getUserByToken(token);
            userId = user.getUserId();
            boolean isVerified = userService.verifyUser(token);
            if (isVerified) {
                return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "http://localhost:3000/user/" + userId + "/password").build();
            } else {
                return ResponseEntity.badRequest().body("Verification failed.");
            }
        } catch (InvalidTokenException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }


    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes req) {
        return ResponseEntity.ok(loginService.login(req));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes req) {
        return ResponseEntity.ok(loginService.refreshToken(req));
    }

    @GetMapping("/get-profile")
    public ResponseEntity<ReqRes> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.info("Fetching profile for email: {}", email); // Log the email being used
        ReqRes response = loginService.getMyInfo(email);
        log.info("Response: {}", response); // Log the response from the service
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/getAll")
    public List<User> list() {
        return userService.getAllUsers();
    }

    @GetMapping("users/{id}")
    User getUserById(@PathVariable Long id) {
        return userService.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @GetMapping("/display/{id}")
    public ResponseEntity<InputStreamResource> displayImage(@PathVariable Long id) {
        Optional<User> optionalUser = userService.findById(id);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        String imagePath = user.getImagePath().replace("\\", "/");

        File file = new File(imagePath);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            String fileName = file.getName();
            String contentType = Files.probeContentType(file.toPath());

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            InputStreamResource resource = new InputStreamResource(fileInputStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=\"" + fileName + "\"")
                    .contentType(MediaType.parseMediaType(contentType))
                    .contentLength(file.length())
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUserById(@RequestPart("user") @Validated(UpdateGroup.class) User newUser,
                                            BindingResult bindingResult,@RequestPart(value = "image", required = false) MultipartFile image,@PathVariable Long id ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }
        Optional<User> optionalUser = userService.findById(id);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if (image != null && !image.isEmpty()) {
            try {
                byte[] bytes = image.getBytes();
                Path path = Paths.get("uploads/" + image.getOriginalFilename());
                Files.write(path, bytes);
                newUser.setImagePath(path.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
            }
        } else {
            // Preserve the existing image path if no new image is uploaded
            newUser.setImagePath(optionalUser.get().getImagePath());
        }
        // Update the user
        User updatedUser = userService.updateUser(id, newUser);
        // Log the user activity for the update
        Long actorId = loginService.userId;
        if (actorId.equals(updatedUser.getUserId())) {
            userActivityLogService.logUserActivity(actorId, updatedUser.getUserId(), "Profile updated");
        }
        else{
            userActivityLogService.logUserActivity(actorId, updatedUser.getUserId(), "User details updated");
        }
        return ResponseEntity.ok(" User is updated");
    }
    @PatchMapping("/updateStatus/{UserId}")
    public ResponseEntity<?> updateStatus(@PathVariable long UserId) {
        User status = userService.updateUserStatus(UserId);
        // Log the user activity for the update
        Long actorId = loginService.userId;
        userActivityLogService.logUserActivity(actorId, status.getUserId(), "User marked as inactive");
        return ResponseEntity.ok(" User is updated");
    }

    @PatchMapping("/updateStatusActive/{UserId}")
    public ResponseEntity<?> updateStatusActive(@PathVariable long UserId) {
        User status = userService.updateUserStatusActive(UserId);
        // Log the user activity for the update
        Long actorId = loginService.userId;
        userActivityLogService.logUserActivity(actorId, status.getUserId(), "User marked as active");
        return ResponseEntity.ok(" User is updated");
    }
    @PostMapping("/{id}/password")
    public ResponseEntity<?> createPassword(@PathVariable("id") Long id, @RequestBody @Validated(CreatePasswordGroup.class)
    User newuser, BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, List<String>> errors = bindingResult.getFieldErrors().stream().collect(Collectors.groupingBy(FieldError::getField, Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())));
            return ResponseEntity.badRequest().body(errors);
        }
        if (!newuser.getPassword().equals(newuser.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("New password and confirm password do not match");
        }
        User updatedUser = userService.createPassword(userId, newuser.getPassword());
        return ResponseEntity.ok(updatedUser);
    }
    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable Long id, @RequestBody @Validated(UpdatePasswordGroup.class) User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, List<String>> errors = bindingResult.getFieldErrors().stream().collect(Collectors.groupingBy(FieldError::getField, Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())));
            return ResponseEntity.badRequest().body(errors);
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("New password and confirm password do not match");
        }
        User updatedUser = userService.updatePassword(id, user.getPassword());
        // Log the user activity for the update
        Long actorId = loginService.userId;
        userActivityLogService.logUserActivity(actorId, updatedUser.getUserId(), "Changed password");
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

