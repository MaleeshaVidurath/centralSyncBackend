package CentralSync.demo.service;
import CentralSync.demo.model.User;
import CentralSync.demo.exception.UserNotFoundException;
import CentralSync.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
<<<<<<< HEAD
import CentralSync.demo.model.UserStatus;
=======
>>>>>>> origin

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;


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


