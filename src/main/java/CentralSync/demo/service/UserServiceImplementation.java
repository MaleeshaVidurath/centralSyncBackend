package CentralSync.demo.service;

import CentralSync.demo.exception.InvalidTokenException;
import CentralSync.demo.exception.UserNotFoundException;
import CentralSync.demo.model.EmailConfirmationToken;
import CentralSync.demo.model.User;
import CentralSync.demo.model.UserStatus;
import CentralSync.demo.repository.EmailConfirmationTokenRepository;
import CentralSync.demo.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import CentralSync.demo.model.UserStatus;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.binary.Base64;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImplementation implements  UserDetailsService,UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailConfirmationTokenRepository emailConfirmationTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;
    private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(15);
    private static final Charset US_ASCII = Charset.forName("US-ASCII");


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow();
    }

    @Override
    public User saveUser(User user) {
        //encode password
        BCryptPasswordEncoder bcrypt=new BCryptPasswordEncoder();
        String encryptedPwd = bcrypt.encode(user.getPassword());
        user.setPassword(encryptedPwd);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }


    @Override
    public User updateUser(Long id, User newUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFirstName(newUser.getFirstName());
                    user.setLastName(newUser.getLastName());
                    user.setRole(newUser.getRole());
                    user.setMobileNo(newUser.getMobileNo());
                    user.setEmail(newUser.getEmail());
                    user.setDateOfBirth(newUser.getDateOfBirth());
                    user.setAddress(newUser.getAddress());
                    user.setDepartment(newUser.getDepartment());
                    user.setTelNo(newUser.getTelNo());
                    //user.setWorkSite(newUser.getWorkSite());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User updateUserStatus(long UserId) {
        return userRepository.findById(UserId)
                .map(user -> {
                    user.setStatus(UserStatus.INACTIVE);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException(UserId));
    }
    @Override
    public User updatePassword(long UserId, String newPassword){
        return userRepository.findById(UserId)
                .map(user -> {
                    BCryptPasswordEncoder bcrypt=new BCryptPasswordEncoder();
                    String encryptedPwd = bcrypt.encode(newPassword);
                    user.setPassword(encryptedPwd);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException(UserId));
    }

    @Override
    public String deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        return "User with id " + id + " has been deleted successfully";
    }

    public void sendRegistrationConfirmationEmail(User user){
        // Generate the token
        String tokenValue = new String(Base64.encodeBase64URLSafe(DEFAULT_TOKEN_GENERATOR.generateKey()), US_ASCII);
        EmailConfirmationToken emailConfirmationToken = new EmailConfirmationToken();
        emailConfirmationToken.setToken(tokenValue);
        emailConfirmationToken.setTimeStamp(LocalDateTime.now());
        emailConfirmationToken.setUser(user);
        emailConfirmationTokenRepository.save(emailConfirmationToken);
        // Send email
        String confirmationUrl = "http://localhost:8080/user/verify?token=" + tokenValue; // Update with your verification URL
        String message = "Click the link to verify your email: " + confirmationUrl;
        emailSenderService.sendSimpleEmail(user.getEmail(), "Email Verification", message);
    }

    @Override
    public boolean verifyUser(String token) throws InvalidTokenException {
        EmailConfirmationToken emailConfirmationToken = emailConfirmationTokenRepository.findByToken(token);
        if (Objects.isNull(emailConfirmationToken) || !token.equals(emailConfirmationToken.getToken())) {
            throw new InvalidTokenException("Token is not valid");
        }
        User user = emailConfirmationToken.getUser();
        if (Objects.isNull(user)) {
            return false;
        }
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        emailConfirmationTokenRepository.delete(emailConfirmationToken);
        return true;
    }

    @Override
    public void generateVerificationToken(User user, String token) {
        EmailConfirmationToken emailConfirmationToken = new EmailConfirmationToken();
        emailConfirmationToken.setToken(token);
        emailConfirmationToken.setTimeStamp(LocalDateTime.now());
        emailConfirmationToken.setUser(user);
        emailConfirmationTokenRepository.save(emailConfirmationToken);
    }

    @Override
    public int getCountOfUser() {
        return userRepository.countUser();
    }


}


