package CentralSync.demo.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    // Send a simple text email
    public void sendSimpleEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("centralsync2024@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
        System.out.println("Simple Mail Sent...");
    }

    // Send a rich MIME email (for HTML content, attachments, etc.)
    public void sendMimeEmail(String toEmail, String subject, String body,String link) throws MessagingException, jakarta.mail.MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("centralsync2024@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(body + "<br><br><a href='" + link + "'>Click here to confirm Item delivery</a>", true); // true indicates HTML
        mailSender.send(message);
        System.out.println("MIME Mail Sent...");
    }
}
