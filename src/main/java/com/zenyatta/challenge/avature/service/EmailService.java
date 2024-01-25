package com.zenyatta.challenge.avature.service;

import com.zenyatta.challenge.avature.model.JobPosting;
import java.io.Serializable;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailService implements Serializable {
    @Value("${email.username}")
    private String username;

    @Value("${email.password}")
    private String password;

    @Value("${email.smtp-host}")
    private String smtpServer;

    @Value("${email.port}")
    private String port;

    public void sendAlert(final JobPosting jobPosting, final String email) {
        if (username == null || username.isEmpty()) {
            return;
        }

        final Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpServer);
        props.put("mail.smtp.port", port);

        final Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        final String bodyText = "This is an alert for a new job posted!" +
                "\nThe job is '" + jobPosting.getTitle() + "' in '" + jobPosting.getLocation() +
                "' and with the skills of " + jobPosting.getSkills() + " for $" + jobPosting.getSalary() + "\n" +
                "\n\nYou received this email because your criteria matches the post.";

        try {
            final Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("forum11593@gmail.com"));
            message.setSubject("New Job Posted!");
            message.setText(bodyText);

            Transport.send(message);
        } catch (final MessagingException exception) {
            if (log.isErrorEnabled()) {
                log.error("An error occured when sending an alert email", exception);
            }
        }
    }
}
