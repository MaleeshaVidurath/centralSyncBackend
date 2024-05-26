//package CentralSync.demo.config;
//
//import CentralSync.demo.model.User;
//import CentralSync.demo.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
//@Component
//public class UserInfoUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private UserRepository repository;
//
//    @Override
//    public UserDetails loadUserByUsername(String firstName) throws UsernameNotFoundException {
//        Optional<User>user= repository.findByFirstName(firstName);
//
//        return
//                user.map(UserInfoUserDetails::new).orElseThrow(()->new UsernameNotFoundException("User not found"));
//
//    }
//}
//
//
