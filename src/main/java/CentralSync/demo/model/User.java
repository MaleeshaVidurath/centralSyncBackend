package CentralSync.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Date;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z\\s]*$", message = "First name is required & must contain only letters")
    private String firstName;
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z\\s]*$", message = "Last name is required & must contain only letters")
    private String lastName;
    @NotBlank(message = "Role is required")
    private String role;



    @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
    private String mobileNo;
    @Pattern(regexp = "\\d{10}", message = "Telephone number must be 10 digits")
    private String telNo;
    @NotBlank(message = "email address is required")
    @Email(message = "Invalid email address")

    private String email;

   // @Past(message = "Date of birth must be in the past")
    @Past(message = "Date should be past")
    @NotNull(message = "Date is required")
    private LocalDate dateOfBirth;
    @NotBlank(message = "Adress is required")
    private String address;
    @NotBlank(message = "Department is required")
    private String department;


    @NotBlank(message = "Password is required")
    private String password;
    //private String workSite;



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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
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





}

