package app.yapam.common.service;

import app.yapam.common.error.InvalidEmailRecipientException;
import app.yapam.config.YapamProperties;
import app.yapam.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired private YapamProperties yapamProperties;
    @Autowired private JavaMailSender javaMailSender;

    public void sendWelcomeMail(User user) {
        var message = new SimpleMailMessage();
        message.setFrom(yapamProperties.getMail().getMessageSender());
        message.setTo(user.getEmail());
        message.setSubject("welcome");
        message.setText("welcome");
        try {
            javaMailSender.send(message);
        } catch (MailSendException e) {
            throw new InvalidEmailRecipientException();
        }
    }
}
