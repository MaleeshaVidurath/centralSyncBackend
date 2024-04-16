package CentralSync.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

import java.util.Date;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long userId;
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "last name is required")
    private String lastName;
    @NotBlank(message = "Role is required")
    private String role;



    @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
    private String mobileNo;

    private String telNo;
    @NotBlank(message = "email address is required")
    @Email(message = "Invalid email address")

    private String email;

    @Past(message = "Date of birth must be in the past")

    private Date dateOfBirth;
    @NotBlank(message = "Adress is required")
    private String address;
    @NotBlank(message = "Department is required")
    private String department;
    private String workSite;



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

    public String getWorkSite() {return workSite;}

    public void setWorkSite(String workSite) {this.workSite = workSite;}
}
