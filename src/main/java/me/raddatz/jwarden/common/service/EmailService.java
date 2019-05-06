package me.raddatz.jwarden.common.service;

import me.raddatz.jwarden.config.AppParameter;
import me.raddatz.jwarden.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired private AppParameter appParameter;
    @Autowired private JavaMailSender javaMailSender;

    public void sendRegisterEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("kevin@familie-raddatz.de");
        message.setTo(user.getEmail());
        message.setSubject("register");
        String registerUrl = appParameter.getHost() + "/users/" + user.getId() + "/email/verify?token=" + user.getEmailToken();
        message.setText(registerUrl);
        javaMailSender.send(message);
    }
}
