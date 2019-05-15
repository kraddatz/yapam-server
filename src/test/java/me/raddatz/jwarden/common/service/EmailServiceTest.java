package me.raddatz.jwarden.common.service;

import me.raddatz.jwarden.config.AppParameter;
import me.raddatz.jwarden.user.model.User;
import me.raddatz.jwarden.user.repository.UserDBO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EmailService.class)
@ActiveProfiles("test")
class EmailServiceTest {

    @Autowired private EmailService emailService;
    @MockBean private AppParameter appParameter;
    @MockBean private JavaMailSender javaMailSender;

    private User getDefaultUser() {
        var user = new User();
        user.setEmail("user@email.com");
        user.setId("userid");
        user.setEmailToken("emailtoken");
        return user;
    }

    @Test
    void sendRegisterEmail() {
        var user = getDefaultUser();

        when(appParameter.getHost()).thenReturn("http://host");

        emailService.sendRegisterEmail(user);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        Mockito.verify(javaMailSender).send(captor.capture());
        var message = captor.getValue();

        assertEquals("kevin@familie-raddatz.de", message.getFrom());
        if (!Objects.isNull(message.getTo())) {
            assertTrue(Arrays.asList(message.getTo()).contains("user@email.com"));
        }
        assertEquals("register", message.getSubject());
        assertEquals("http://host/users/userid/email/verify?token=emailtoken", message.getText());
    }

    @Test
    void sendEmailChangeRequest() {
        var user = getDefaultUser();
        var email = "email";

        when(appParameter.getHost()).thenReturn("http://host");

        emailService.sendEmailChangeEmail(user, email);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        Mockito.verify(javaMailSender).send(captor.capture());
        var message = captor.getValue();

        assertEquals("kevin@familie-raddatz.de", message.getFrom());
        if (!Objects.isNull(message.getTo())) {
            assertTrue(Arrays.asList(message.getTo()).contains("email"));
        }
        assertEquals("verify your email", message.getSubject());
        assertEquals("http://host/users/userid/email/change?email=email&token=emailtoken", message.getText());
    }
}