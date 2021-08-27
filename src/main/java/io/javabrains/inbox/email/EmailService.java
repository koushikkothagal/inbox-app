package io.javabrains.inbox.email;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.javabrains.inbox.emailslist.EmailsList;
import io.javabrains.inbox.emailslist.EmailsListPrimaryKey;
import io.javabrains.inbox.emailslist.EmailsListRepository;
import io.javabrains.inbox.folders.UnreadEmailStatsRepository;

@Service
public class EmailService {

    @Autowired
    private EmailsListRepository emailsListRepository;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private UnreadEmailStatsRepository unreadEmailStatsRepository;

    public void sendEmail(String fromUserId, String toUserIds, String subject, String body) {

        UUID timeUuid = Uuids.timeBased();

        List<String> toUserIdList = Arrays.asList(toUserIds.split(",")).stream()
                .map(id -> StringUtils.trimWhitespace(id)).filter(id -> StringUtils.hasText(id))
                .collect(Collectors.toList());
        // Add to sent items of sender
        EmailsList sentItemEntry = prepareEmailsListEntry("Sent", fromUserId, fromUserId, toUserIdList, subject,
                timeUuid);
        sentItemEntry.setRead(true);
        emailsListRepository.save(sentItemEntry);
        // Add to inbox of each reciever
        toUserIdList.stream().forEach(toUserId -> {
            EmailsList inboxEntry = prepareEmailsListEntry("Inbox", toUserId, fromUserId, toUserIdList, subject,
                    timeUuid);
            inboxEntry.setRead(false);
            emailsListRepository.save(inboxEntry);
            unreadEmailStatsRepository.incrementUnreadCounter(toUserId, "Inbox");
        });

        // Save email entity
        Email email = new Email();
        email.setId(timeUuid);
        email.setFrom(fromUserId);
        email.setTo(toUserIdList);
        email.setSubject(subject);
        email.setBody(body);
        emailRepository.save(email);

    }

    private EmailsList prepareEmailsListEntry(String folderName, String forUser, String fromUserId,
            List<String> toUserIds, String subject, UUID timeUuid) {

        EmailsListPrimaryKey key = new EmailsListPrimaryKey();
        key.setLabel(folderName);
        key.setUserId(forUser);
        key.setTimeId(timeUuid);
        EmailsList emailsListEntry = new EmailsList();
        emailsListEntry.setId(key);
        emailsListEntry.setFrom(fromUserId);
        emailsListEntry.setTo(toUserIds);
        emailsListEntry.setSubject(subject);
        return emailsListEntry;
    }

}
