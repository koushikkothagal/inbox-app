package io.javabrains.inbox;

import java.nio.file.Path;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.javabrains.inbox.emailslist.EmailsList;
import io.javabrains.inbox.emailslist.EmailsListPrimaryKey;
import io.javabrains.inbox.emailslist.EmailsListRepository;

@SpringBootApplication
@RestController
public class SpringGitHubLoginApplication {

	@Autowired
	private EmailsListRepository emailsListRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringGitHubLoginApplication.class, args);
	}

	@RequestMapping("/user")
	public String user(@AuthenticationPrincipal OAuth2User principal) {
		System.out.println(principal);
		return principal.getAttribute("name");
	}

	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
		Path bundle = astraProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}

	@PostConstruct
	public void initializeData() {

		for (int i = 0; i < 10; i++) {

			EmailsListPrimaryKey key = new EmailsListPrimaryKey();
			key.setUserId("koushikkothagal");
			key.setLabel("Inbox");
			key.setTimeId(Uuids.timeBased());

			EmailsList emailsListEntry = new EmailsList();
			emailsListEntry.setId(key);
			emailsListEntry.setFrom("testuser" + i);
			emailsListEntry.setSubject("Hello " + i);
			emailsListEntry.setRead(false);
			emailsListEntry.setTo(Arrays.asList("koushikkothagal"));
			

			emailsListRepository.save(emailsListEntry);
		}
		System.out.println(emailsListRepository.findAllById_UserIdAndId_Label("koushikkothagal", "Inbox"));

	}

}
