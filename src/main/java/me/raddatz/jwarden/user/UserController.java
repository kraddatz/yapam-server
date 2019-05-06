package me.raddatz.jwarden.user;

import me.raddatz.jwarden.user.model.RegisterUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired private UserService userService;

//    @VerifiedEmail
    @PostMapping(value = "register", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public RegisterUser register(@RequestBody RegisterUser model) {
        return userService.register(model);
    }

    @GetMapping(value = "{userId}/email/verify")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void verifyEmail(@PathVariable(value = "userId") String userId,
                            @RequestParam(value = "token") String token) {
        userService.verifyEmail(userId, token);
    }
}
