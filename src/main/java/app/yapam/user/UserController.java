package app.yapam.user;

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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {

    @Autowired private UserService userService;

    @ApiOperation(value = "Create a user")
    @PostMapping(value = "/api/users", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public UserResponse createUser(@RequestBody UserRequest user) {
        return userService.createUser(user);
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

    @GetMapping(value = "/logout")
    public void logout(HttpServletRequest request) throws ServletException {
        request.logout();
    }
}
