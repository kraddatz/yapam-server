package app.yapam.common.service;

import app.yapam.YapamBaseTest;
import app.yapam.config.AppParameter;
import app.yapam.config.YapamProperties;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EmailService.class)
@ActiveProfiles("test")
class EmailServiceTest extends YapamBaseTest {

    @Autowired private EmailService emailService;
    @Autowired private YapamProperties yapamProperties;
    @MockBean private AppParameter appParameter;
    @MockBean private JavaMailSender javaMailSender;

    @Test
    void sendRegisterEmail() {
        var user = createDefaultUser();
        when(appParameter.getHost()).thenReturn(DEFAULT_HOST_BASE_URL);

        emailService.sendRegisterEmail(user);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        Mockito.verify(javaMailSender).send(captor.capture());
        var message = captor.getValue();

        assertEquals(yapamProperties.getMail().getMessageSender(), message.getFrom());
        if (!Objects.isNull(message.getTo())) {
            assertTrue(Arrays.asList(message.getTo()).contains(DEFAULT_USER_EMAIL));
        } else {
            fail();
        }
        assertEquals("register", message.getSubject());
        assertEquals(String.format(EMAIL_GENERIC_EMAIL_VERIFY_URL, user.getId(), user.getEmailToken()), message.getText());
    }

    @Test
    void sendEmailChangeRequest() {
        var user = createDefaultUser();
        var newEmail = "newemail@email.com";
        when(appParameter.getHost()).thenReturn(DEFAULT_HOST_BASE_URL);

        emailService.sendEmailChangeEmail(user, newEmail);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        Mockito.verify(javaMailSender).send(captor.capture());
        var message = captor.getValue();

        assertEquals(yapamProperties.getMail().getMessageSender(), message.getFrom());
        if (!Objects.isNull(message.getTo())) {
            assertTrue(Arrays.asList(message.getTo()).contains(newEmail));
        }
        assertEquals("verify your email", message.getSubject());
        assertEquals(String.format(EMAIL_GENERIC_EMAIL_CHANGE_URL, user.getId(), newEmail, user.getEmailToken()), message.getText());
    }
}