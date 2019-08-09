package app.yapam.common.service;

import app.yapam.YapamBaseTest;
import app.yapam.common.error.InvalidEmailRecipientException;
import app.yapam.config.YapamProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EmailService.class)
@ActiveProfiles("test")
class EmailServiceTest extends YapamBaseTest {

    @Autowired private EmailService emailService;
    @MockBean private YapamProperties yapamProperties;
    @MockBean private JavaMailSender javaMailSender;

    @Test
    void sendRegisterEmail_whenInvalidRecipient_thenThrowException() {
        var user = createDefaultUser();
        doThrow(MailSendException.class).when(javaMailSender).send(any(SimpleMailMessage.class));
        when(yapamProperties.getHost()).thenReturn(DEFAULT_HOST_BASE_URL);
        when(yapamProperties.getMail()).thenReturn(mock(YapamProperties.YapamMail.class));
        when(yapamProperties.getMail().getMessageSender()).thenReturn(EMAIL_MESSAGE_SENDER);

        assertThrows(InvalidEmailRecipientException.class, () -> emailService.sendWelcomeMail(user));
    }

    @Test
    void sendRegisterEmail() {
        var user = createDefaultUser();
        when(yapamProperties.getHost()).thenReturn(DEFAULT_HOST_BASE_URL);
        when(yapamProperties.getMail()).thenReturn(mock(YapamProperties.YapamMail.class));
        when(yapamProperties.getMail().getMessageSender()).thenReturn(EMAIL_MESSAGE_SENDER);

        emailService.sendWelcomeMail(user);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        Mockito.verify(javaMailSender).send(captor.capture());
        var message = captor.getValue();

        assertEquals(yapamProperties.getMail().getMessageSender(), message.getFrom());
        if (!Objects.isNull(message.getTo())) {
            assertTrue(Arrays.asList(message.getTo()).contains(DEFAULT_USER_EMAIL));
        } else {
            fail();
        }
        assertEquals("welcome", message.getSubject());
        assertEquals(EMAIL_WELCOME_TEXT, message.getText());
    }
}
