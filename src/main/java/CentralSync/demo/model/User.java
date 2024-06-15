package CentralSync.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import CentralSync.demo.validation.ValidPassword;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z\\s]*$", message = "First name is required & must contain only letters", groups = {CreateGroup.class, UpdateGroup.class})
    private String firstName;
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z\\s]*$", message = "Last name is required & must contain only letters", groups = {CreateGroup.class, UpdateGroup.class})
    private String lastName;
    @NotBlank(message = "Role is required", groups = {CreateGroup.class, UpdateGroup.class})
    private String role;
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits", groups = {CreateGroup.class, UpdateGroup.class})
    private String mobileNo;
    @Pattern(regexp = "\\d{10}", message = "Telephone number must be 10 digits", groups = {CreateGroup.class, UpdateGroup.class})
    private String telNo;
    @NotBlank(message = "email address is required", groups = {CreateGroup.class, UpdateGroup.class})
    @Email(message = "Invalid email address", groups = {CreateGroup.class, UpdateGroup.class})
    private String email;
    @Past(message = "Date should be past", groups = {CreateGroup.class, UpdateGroup.class})
    @NotNull(message = "Date of birth is required", groups = {CreateGroup.class, UpdateGroup.class})
    private LocalDate dateOfBirth;
    @NotBlank(message = "Address is required", groups = {CreateGroup.class, UpdateGroup.class})
    private String address;
    @NotBlank(message = "Department is required", groups = {CreateGroup.class, UpdateGroup.class})
    private String department;
    @NotBlank(message = "Worksite is required", groups = {CreateGroup.class, UpdateGroup.class})
    private String workSite;
    @ValidPassword(groups = {CreatePasswordGroup.class,UpdatePasswordGroup.class})
    @NotBlank(message = "Password is required", groups = {CreatePasswordGroup.class, UpdatePasswordGroup.class})
    private String password;
    @Transient
    @NotBlank(message = "Confirm password is required", groups = {CreatePasswordGroup.class,UpdatePasswordGroup.class})
    private String confirmPassword;
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<InventoryRequest> inventoryRequests;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    //public String getWorkSite() {return workSite;}

    //public void setWorkSite(String workSite) {this.workSite = workSite;}


}

