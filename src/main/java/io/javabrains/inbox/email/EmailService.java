package io.javabrains.inbox.email;

import java.util.Arrays;
import java.util.UUID;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.javabrains.inbox.emailslist.EmailsList;
import io.javabrains.inbox.emailslist.EmailsListPrimaryKey;
import io.javabrains.inbox.emailslist.EmailsListRepository;

@Service
public class EmailService {

    @Autowired
    private EmailsListRepository emailsListRepository;

    @Autowired
    private EmailRepository emailRepository;

    public void sendEmail(String fromUserId, String toUserId, String subject, String body) {

        UUID timeUuid = Uuids.timeBased();
        // Add to sent items of sender
        EmailsList sentItemEntry = prepareEmailsListEntry("Sent", fromUserId, toUserId, subject, timeUuid);
        emailsListRepository.save(sentItemEntry);
        // Add to inbox of each reciever
        EmailsList inboxEntry = prepareEmailsListEntry("Inbox", fromUserId, toUserId, subject, timeUuid);
        emailsListRepository.save(inboxEntry);
        // Save email entity
        Email email = new Email();
        email.setId(timeUuid);
        email.setFrom(fromUserId);
        email.setTo(Arrays.asList(toUserId));
        email.setSubject(subject);
        email.setBody(body);
        emailRepository.save(email);
        
    }

    private EmailsList prepareEmailsListEntry(String folderName, String fromUserId, String toUserId, String subject, UUID timeUuid) {
        EmailsListPrimaryKey key = new EmailsListPrimaryKey();
        key.setLabel(folderName);
        key.setUserId(fromUserId);
        key.setTimeId(timeUuid);
        EmailsList emailsListEntry = new EmailsList();
        emailsListEntry.setId(key);
        emailsListEntry.setFrom(fromUserId);
        emailsListEntry.setTo(toUserId);
        emailsListEntry.setSubject(subject);
        return emailsListEntry;
    }
    
}
