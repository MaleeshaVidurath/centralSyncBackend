package CentralSync.demo;

import CentralSync.demo.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
@RequestMapping("/file")
@EnableJpaRepositories(basePackages = "CentralSync.demo.repository")
@EntityScan(basePackages = "CentralSync.demo.model")
public class CentralSyncBackend {
	@Autowired
	private EmailSenderService senderService;

	public static void main(String[] args) {

		SpringApplication.run(CentralSyncBackend.class, args);
	}
}

