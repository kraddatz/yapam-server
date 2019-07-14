package app.yapam.stepDefinitions;

import app.yapam.ContextHolderService;
import app.yapam.YapamSteps;
import app.yapam.common.service.EmailService;
import app.yapam.user.repository.UserDBO;
import app.yapam.user.repository.UserRepository;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;

public class CommonStepsDefs extends YapamSteps {

    @Autowired private UserRepository userRepository;
    @Autowired private ContextHolderService context;
    @Autowired private JavaMailSender javaMailSender;
    @LocalServerPort protected Integer port;
//    @MockBean private EmailService emailService;

    @Given("The email does not exist")
    public void theEmailDoesNotExist() {
        doThrow(MailSendException.class).when(javaMailSender).send(any(SimpleMailMessage.class));
    }

    @Given("user with data exists")
    public void user_with_data_exists(Map<String, String> data) {
        var userDBO = new UserDBO();
        userDBO.setName(data.getOrDefault("name", "Max Mustermann"));
        userDBO.setPublicKey(data.getOrDefault("publicKey", "publicKey"));
        userDBO.setMasterPasswordHint(data.getOrDefault("masterPasswordHint", "passwordispassword"));
        userDBO.setMasterPasswordHash(data.getOrDefault("masterPasswordHash", "$2a$10$HFeBQjv4d.iGubQvGZe31uMBxWqoaHLQt9O1na7KlFZKhxvPkf7ge"));
        userDBO.setEmailVerified("true".equals(data.getOrDefault("verified", "true")));
        userDBO.setEmail(data.getOrDefault("email", "max.mustermann@email.com"));
        userDBO.setCulture("de-DE");
        userDBO.setCreationDate(LocalDateTime.now());
        userDBO.setEmailToken(UUID.randomUUID().toString());
        userRepository.save(userDBO);
    }

    @Given("request with body")
    public void with_data(String payload) {
        context.setRequest(given().baseUri("http://localhost").port(port).body(payload).contentType(ContentType.JSON));
    }

    @When("the user posts data to {string}")
    public void the_user_posts_to(String url) {
        context.setResponse(context.getRequest().when().post(url));
    }

    @Then("status is {int}")
    public void status_is(Integer status) {
        context.getResponse().then().statusCode(status);
    }

    @After
    public void tearDown() {
        userRepository.deleteAll();
        reset(javaMailSender);
    }
}
