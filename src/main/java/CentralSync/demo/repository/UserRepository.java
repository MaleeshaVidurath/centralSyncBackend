package CentralSync.demo.repository;

import CentralSync.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @Query("SELECT COUNT(u) FROM User u")
    int countUser();

    Optional<User> findByEmail(String email);

//    User findByEmail(String email);
}
