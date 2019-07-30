package app.yapam.user;

import app.yapam.user.model.request.PasswordChangeRequest;
import app.yapam.user.model.request.UserRequest;
import app.yapam.user.model.response.SimpleUserResponse;
import app.yapam.user.model.response.SimpleUserResponseWrapper;
import app.yapam.user.model.response.UserResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired private UserService userService;

    @ApiOperation(value = "Create a user")
    @PostMapping(value = "/api/users", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public UserResponse createUser(@RequestBody UserRequest user) {
        return userService.createUser(user);
    }

    @ApiOperation(value = "Do change the email with verification token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "Internal id of the user", required = true),
            @ApiImplicitParam(name = "token", value = "Token for verification", required = true),
            @ApiImplicitParam(name = "email", value = "The new email", required = true)
    })
    @GetMapping(value = "/api/users/{userId}/email/change")
    public void emailChange(@PathVariable("userId") String userId,
                            @RequestParam(value = "token") String token,
                            @RequestParam(value = "email") String email) {
        userService.emailChange(userId, token, email);
    }

    @ApiOperation(value = "Get all users reachable by the user")
    @GetMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SimpleUserResponseWrapper getAllUsers() {
        return userService.getAllUsers();
    }

    @ApiOperation(value = "Get the current user")
    @GetMapping(value = "/api/users/currentuser", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserResponse getCurrentUser() {
        return userService.getCurrentUser();
    }

    @ApiOperation(value = "Get a user by id")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "userId", value = "Internal id of the user", required = true)
    )
    @GetMapping(value = "/api/users/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SimpleUserResponse getUserById(@PathVariable("userId") String userId) {
        return userService.getSimpleUserById(userId);
    }

    @ApiOperation(value = "Request to change the email")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "email", value = "The new email", required = true)
    )
    @GetMapping(value = "/api/users/currentuser/email/request-change")
    public void requestEmailChange(@RequestParam(value = "email") String email) {
        userService.requestEmailChange(email);
    }

    @ApiOperation(value = "Change the users password")
    @PutMapping(value = "/api/users/currentuser/password/change", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updatePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        userService.passwordChange(passwordChangeRequest);
    }

    @ApiOperation(value = "Verify the email of a user")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "Internal id of the user", required = true),
            @ApiImplicitParam(name = "token", value = "Token for verification", required = true)
    })
    @GetMapping(value = "/api/users/{userId}/email/verify")
    public void verifyEmail(@PathVariable("userId") String userId,
                            @RequestParam(value = "token") String token) {
        userService.verifyEmail(userId, token);
    }
}
