package CentralSync.demo.model;

import CentralSync.demo.validation.ValidPassword;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    @NotBlank(message = "Role is required", groups = {CreateGroup.class})
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Address is required", groups = {CreateGroup.class, UpdateGroup.class})
    private String address;

    @NotBlank(message = "Department is required", groups = {CreateGroup.class, UpdateGroup.class})
    private String department;

    @NotBlank(message = "Worksite is required", groups = {CreateGroup.class, UpdateGroup.class})
    private String workSite;

    @ValidPassword(groups = {CreatePasswordGroup.class, UpdatePasswordGroup.class})
    @NotBlank(message = "Password is required", groups = {CreatePasswordGroup.class, UpdatePasswordGroup.class})
    private String password;

    @Transient
    @NotBlank(message = "Confirm password is required", groups = {CreatePasswordGroup.class, UpdatePasswordGroup.class})
    private String confirmPassword;

    @Enumerated(EnumType.STRING)
    private UserStatus status;
    //@NotBlank(message = "Image is required", groups = {CreateGroup.class, UpdateGroup.class})
    private String imagePath;

//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JsonManagedReference("user-inventoryRequests")
//    private List<InventoryRequest> inventoryRequests;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role.split(","))
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @JsonProperty("authorities")
    public List<String> getAuthoritiesAsString() {
        return List.of(role.split(","));
    }

    @JsonProperty("authorities")
    public void setAuthoritiesAsString(List<String> authorities) {
        this.role = String.join(",", authorities);
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
}
