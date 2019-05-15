package me.raddatz.jwarden.user;

import me.raddatz.jwarden.common.annotation.userexists.UserExists;
import me.raddatz.jwarden.user.model.User;
import me.raddatz.jwarden.user.model.RegisterUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired private UserService userService;

    @PostMapping(value = "users", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public User createUser(@RequestBody RegisterUser model) {
        return userService.createUser(model);
    }

    @GetMapping(value = "users/{userId}/email/verify")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @UserExists
    public void verifyEmail(@PathVariable(value = "userId") String userId,
                            @RequestParam(value = "token") String token) {
        userService.verifyEmail(userId, token);
    }

    @GetMapping(value = "users/{userId}/email/requestChange")
    @UserExists
    public void requestEmailChange(@PathVariable(value = "userId") String userId,
                                   @RequestParam(value = "email") String email) {
        userService.requestEmailChange(userId, email);
    }

    @GetMapping(value = "users/{userId}/email/change")
    @UserExists
    public void emailChange(@PathVariable(value = "userId") String userId,
                            @RequestParam(value = "token") String token,
                            @RequestParam(value = "email") String email) {
        userService.emailChange(userId, token, email);
    }
}