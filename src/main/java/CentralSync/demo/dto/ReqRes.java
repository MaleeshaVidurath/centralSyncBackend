package CentralSync.demo.dto;


import CentralSync.demo.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqRes {

    private Long userId;
    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String firstName;
    private String lastName;
    private String role;
    private String mobileNo;
    private String telNo;
    private String email;
    private LocalDate dateOfBirth;
    private String address;
    private String department;
    private String password;
    private String confirmPassword;
    private User users;
    private List<User> usersList;


}
