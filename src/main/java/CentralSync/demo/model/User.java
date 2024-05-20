package CentralSync.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

import java.util.Date;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z\\s]*$", message = "First name is required & must contain only letters",groups = {CreateGroup.class, UpdateGroup.class})
    private String firstName;
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z\\s]*$", message = "Last name is required & must contain only letters",groups = {CreateGroup.class, UpdateGroup.class})
    private String lastName;
    @NotBlank(message = "Role is required",groups = {CreateGroup.class, UpdateGroup.class})
    private String role;



    @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits",groups = {CreateGroup.class, UpdateGroup.class})
    private String mobileNo;
    @Pattern(regexp = "\\d{10}", message = "Telephone number must be 10 digits",groups = {CreateGroup.class, UpdateGroup.class})
    private String telNo;
    @NotBlank(message = "email address is required",groups = {CreateGroup.class, UpdateGroup.class})
    @Email(message = "Invalid email address",groups = {CreateGroup.class, UpdateGroup.class})

    private String email;

   // @Past(message = "Date of birth must be in the past")
    @Past(message = "Date should be past",groups = {CreateGroup.class, UpdateGroup.class})
    @NotNull(message = "Date of birth is required",groups = {CreateGroup.class, UpdateGroup.class})
    private Date dateOfBirth;
    @NotBlank(message = "Address is required",groups = {CreateGroup.class, UpdateGroup.class})
    private String address;
    @NotBlank(message = "Department is required",groups = {CreateGroup.class, UpdateGroup.class})
    private String department;


    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@#$%^&+=]).{8,}$", message = "Password must be at least 8 characters long and strong",groups = {UpdateGroup.class})
    private String password;
    //private String workSite;

    @Enumerated(EnumType.STRING)
    private UserStatus status;



    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getTelNo() {return telNo;}

    public void setTelNo(String telNo) {this.telNo = telNo;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDepartment() {return department;}

    public void setDepartment(String department) {this.department = department;}

    //public String getWorkSite() {return workSite;}

    //public void setWorkSite(String workSite) {this.workSite = workSite;}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserStatus getStatus() {return status;}

    public void setStatus(UserStatus status) {this.status = status;}





}

