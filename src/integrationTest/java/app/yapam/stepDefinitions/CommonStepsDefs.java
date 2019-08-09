package app.yapam.stepDefinitions;

import app.yapam.ContextHolderService;
import app.yapam.YapamSteps;
import app.yapam.user.repository.UserDao;
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

    @Given("an authorized user")
    public void given_anAuthorizedUser() {
        context.getRequest().header("Authorization", "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ4aXVzcDJqdjJKdmEwNGtCRlRSLWZJRG5rMTJzUy1SLVNDalpuTXRkNm5vIn0.eyJqdGkiOiI1Y2JhNDM1Ny01YmM3LTQ1ZGYtYmUwNi1iNTVlMjM3NGY0ZjkiLCJleHAiOjE1NjUzNzY4NDMsIm5iZiI6MCwiaWF0IjoxNTY1Mzc2NTQzLCJpc3MiOiJodHRwczovL2lkZW50aXR5LnJhZGRhdHoubWUvYXV0aC9yZWFsbXMveWFwYW0iLCJhdWQiOlsibW9iaWxlIiwiYWNjb3VudCJdLCJzdWIiOiJhNzQwMTlhYS1lOWQxLTQyMTEtOGM4OC1hZjFlZWI1MjE0MzciLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ3ZWIiLCJhdXRoX3RpbWUiOjAsInNlc3Npb25fc3RhdGUiOiJhZTk4MmVmZi03OTY3LTQxZDgtOGJkZi02OWU2NDlhZWI3MWEiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6ODA4MCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJ3ZWIiOnsicm9sZXMiOlsidXNlciJdfSwibW9iaWxlIjp7InJvbGVzIjpbIlVTRVIiXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIGVtYWlsIHByb2ZpbGUiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6IktldmluIFJhZGRhdHoiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJrZXZpbkBmYW1pbGllLXJhZGRhdHouZGUiLCJnaXZlbl9uYW1lIjoiS2V2aW4iLCJmYW1pbHlfbmFtZSI6IlJhZGRhdHoiLCJlbWFpbCI6ImtldmluQGZhbWlsaWUtcmFkZGF0ei5kZSJ9.hmUDxlXp_zos55qX-DgVCDoEGszduBTOh0xEsC6x7KrxfhcKuPsHeNhNZP742PrqJHo5RGT3RYofmbH1XBJ8spdAgdJHZYm00CkyOGmJ8c-4CX9ASWJzrUjKspDSzTc2MKyIBZrvd9ky00AHBnpUWRaooankCvUVpWilmDkk48aaCLMbz9jOK2ji1xhdLZXeZXGHtksEe2jL4m75TUDxqGKJfLL8PZMSgqfIDrKOl1suVwk2avWyv3qELYpSfw-49I9ilJZSXicOTxFGzTN-X3WRFUkdKETZQ3xQLXwtN2qRNdQkKK89UXajIkDU3ccqMPd4A9w30uDC-Unsr9vngg");
    }

    @Given("an existing user")
    public void given_anExistingUser(Map<String, String> data) {
        var userDBO = new UserDao();
        userDBO.setName(data.getOrDefault("name", "Max Mustermann"));
        userDBO.setPublicKey(data.getOrDefault("publicKey", "publicKey"));
        userDBO.setEmail(data.getOrDefault("email", "max.mustermann@email.com"));
        var creationDate = data.containsKey("creationDate") ? LocalDateTime.parse(data.get("creationDate")) : LocalDateTime.now();
        userDBO.setCreationDate(creationDate);
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
