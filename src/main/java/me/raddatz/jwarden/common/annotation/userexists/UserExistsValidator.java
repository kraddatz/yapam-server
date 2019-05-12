package me.raddatz.jwarden.common.annotation.userexists;

import me.raddatz.jwarden.common.error.UserNotFoundException;
import me.raddatz.jwarden.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Component
public class UserExistsValidator {

    @Autowired private UserRepository userRepository;

    public boolean validate(HttpServletRequest request) {
        var path = request.getRequestURI();
        var userId = path.split("/")[2];
        var user = userRepository.findOneById(userId);
        if (Objects.isNull(user)) {
            throw new UserNotFoundException();
        }
        return true;
    }
}
