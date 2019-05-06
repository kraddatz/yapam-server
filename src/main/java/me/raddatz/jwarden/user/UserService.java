package me.raddatz.jwarden.user;

import me.raddatz.jwarden.common.error.EmailAlreadyExistsException;
import me.raddatz.jwarden.common.error.InvalidEmailVerificationTokenException;
import me.raddatz.jwarden.common.error.UserNotExistsException;
import me.raddatz.jwarden.common.service.EmailService;
import me.raddatz.jwarden.common.service.PBKDF2Service;
import me.raddatz.jwarden.user.model.RegisterUser;
import me.raddatz.jwarden.user.model.User;
import me.raddatz.jwarden.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private EmailService emailService;
    @Autowired private PBKDF2Service pbkdf2Service;

    @Transactional
    void tryToRegisterUser(User user) {
        if (userRepository.findOneByEmail(user.getEmail()) == null) {
            userRepository.save(user);
        } else {
            throw new EmailAlreadyExistsException();
        }
        emailService.sendRegisterEmail(user);
    }

    public RegisterUser register(RegisterUser registerUser) {
        var user = registerUser.toUser();
        user.setMasterPasswordSalt(pbkdf2Service.generateSalt());
        user.setMasterPasswordHash(pbkdf2Service.generateSaltedHash(
                registerUser.getMasterPassword(), user.getMasterPasswordSalt()
        ));
        user.setEmailToken(UUID.randomUUID().toString());
        user.setCreationDate(LocalDateTime.now());
        tryToRegisterUser(user);
        return registerUser;
    }

    @Transactional
    public void verifyEmail(String userId, String token) {
        var user = userRepository.findOneById(userId);
        if (user == null) {
            throw new UserNotExistsException();
        }
        if (user.getEmailToken().equals(token)) {
            user.setEmailVerified(true);
            userRepository.save(user);
        } else {
            throw new InvalidEmailVerificationTokenException();
        }
    }
}
