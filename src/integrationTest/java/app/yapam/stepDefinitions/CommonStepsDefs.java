package app.yapam.stepDefinitions;

import app.yapam.ContextHolderService;
import app.yapam.YapamSteps;
import app.yapam.user.repository.UserDBO;
import app.yapam.user.repository.UserRepository;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;

public class CommonStepsDefs extends YapamSteps {

    @Autowired private UserRepository userRepository;
    @Autowired private ContextHolderService context;
    @Autowired private JavaMailSender javaMailSender;
    @LocalServerPort protected Integer port;

    @Before
    public void setUp() {
        context.setRequest(given().port(port).baseUri("http://localhost"));
    }

    @Given("The email is invalid")
    public void given_theEmailDoesNotExist() {
        doThrow(MailSendException.class).when(javaMailSender).send(any(SimpleMailMessage.class));
    }

    @Given("an existing user")
    public void given_anExistingUser(Map<String, String> data) {
        var userDBO = new UserDBO();
        userDBO.setName(data.getOrDefault("name", "Max Mustermann"));
        userDBO.setPublicKey(data.getOrDefault("publicKey", "publicKey"));
        userDBO.setMasterPasswordHint(data.getOrDefault("masterPasswordHint", "passwordispassword"));
        userDBO.setMasterPasswordHash(data.getOrDefault("masterPasswordHash", "$2a$10$HFeBQjv4d.iGubQvGZe31uMBxWqoaHLQt9O1na7KlFZKhxvPkf7ge"));
        userDBO.setEmailVerified("true".equals(data.getOrDefault("verified", "true")));
        userDBO.setEmail(data.getOrDefault("email", "max.mustermann@email.com"));
        userDBO.setCulture("de-DE");
        var creationDate = data.containsKey("creationDate") ? LocalDateTime.parse(data.get("creationDate")) : LocalDateTime.now();
        userDBO.setCreationDate(creationDate);
        userDBO.setEmailToken(data.getOrDefault("emailToken", UUID.randomUUID().toString()));
        userRepository.save(userDBO);
     }

    @Given("a request body")
    public void given_aRequestBody(String payload) {
        context.getRequest().body(payload).contentType(ContentType.JSON);
    }

    @Given("request parameter")
    public void given_requestParameter(Map<String, String> parameter) {
        context.getRequest().params(parameter);
    }

    @Given("path parameter")
    public void given_pathParameter(Map<String, String> parameter) {
        context.getRequest().pathParams(parameter);
    }

    @Given("userId in path")
    public void given_userIdInPath() {
        var user = userRepository.findAll().get(0);
        context.getRequest().pathParam("userId", user.getId());
    }

    @When("^I send a (get|post) request to (.*)$")
    public void when_iSendARequestTo(String method, String url) {
        Response response;
        switch (method) {
            case "post":
                response = context.getRequest().when().post(url); break;
            case "get":
                response = context.getRequest().when().get(url); break;
            default:
                response = null;
        }
        context.setResponse(response);
    }

    @Then("status is {int}")
    public void then_statusIs(Integer status) {
        context.getResponse().then().statusCode(status);
    }

    @Then("exception is {string}")
    public void then_exceptionIs(String exception) {
        assertEquals(exception, context.getResponse().then().extract().jsonPath().getString("error"));
    }

    @After
    public void tearDown() {
        userRepository.deleteAll();
        reset(javaMailSender);
        context.setResponse(null);
        context.setRequest(null);
    }
}
