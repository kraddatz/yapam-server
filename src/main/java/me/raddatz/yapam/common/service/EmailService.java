package me.raddatz.yapam.common.service;

import me.raddatz.yapam.config.AppParameter;
import me.raddatz.yapam.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired private AppParameter appParameter;
    @Autowired private JavaMailSender javaMailSender;

    public void sendRegisterEmail(User user) {
        var message = new SimpleMailMessage();
        message.setFrom("kevin@familie-raddatz.de");
        message.setTo(user.getEmail());
        message.setSubject("register");
        var registerUrl = appParameter.getHost() + "/users/" + user.getId() + "/email/verify?token=" + user.getEmailToken();
        message.setText(registerUrl);
        javaMailSender.send(message);
    }

    public void sendEmailChangeEmail(User user, String email) {
        var message = new SimpleMailMessage();
        message.setFrom("kevin@familie-raddatz.de");
        message.setTo(email);
        message.setSubject("verify your email");
        var emailChangeUrl = appParameter.getHost() + "/users/" + user.getId() + "/email/change?email=" + email + "&token=" + user.getEmailToken();
        message.setText(emailChangeUrl);
        javaMailSender.send(message);
    }
}
