package CentralSync.demo.service.UserManagementModule;

import CentralSync.demo.exception.UserManagementModule.UserNotFoundException;
import CentralSync.demo.model.UserManagementModule.User;
import CentralSync.demo.model.UserManagementModule.UserStatus;
import CentralSync.demo.repository.UserManagmentModule.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements  UserDetailsService,UserService{

    @Autowired
    private UserRepository userRepository;

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

    @Override
    public int getCountOfUser() {
        return userRepository.countUser();
    }


}

