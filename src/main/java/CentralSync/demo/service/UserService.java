package CentralSync.demo.service;

import CentralSync.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public User saveUser(User user);
    public List<User> getAllUsers();

    Optional<User> findById(Long id);
    public User updateUser(Long id, User newUser);

    public User updateUserStatus (long UserId);

    User updatePassword(long UserId, String newPassword);

    String deleteUser(Long id);

    int getCountOfUser();


    //User updateUser(@RequestBody User newUser, @PathVariable Long id);
}
