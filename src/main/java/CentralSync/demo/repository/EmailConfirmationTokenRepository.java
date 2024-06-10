package CentralSync.demo.repository;

import CentralSync.demo.model.EmailConfirmationToken;
import CentralSync.demo.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailConfirmationTokenRepository extends JpaRepository<EmailConfirmationToken,Long> {
    EmailConfirmationToken findByToken(String token);
    String deleteByToken(String token);
}
