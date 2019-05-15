package me.raddatz.jwarden.user;

import me.raddatz.jwarden.common.error.EmailAlreadyExistsException;
import me.raddatz.jwarden.common.error.EmailVerificationTokenExpiredException;
import me.raddatz.jwarden.common.error.InvalidEmailVerificationTokenException;
import me.raddatz.jwarden.common.service.EmailService;
import me.raddatz.jwarden.common.service.PBKDF2Service;
import me.raddatz.jwarden.config.JWardenProperties;
import me.raddatz.jwarden.user.model.RegisterUser;
import me.raddatz.jwarden.user.model.User;
import me.raddatz.jwarden.user.repository.UserDBO;
import me.raddatz.jwarden.user.repository.UserRepository;
import me.raddatz.jwarden.user.repository.UserTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private EmailService emailService;
    @Autowired private PBKDF2Service pbkdf2Service;
    @Autowired private JWardenProperties jWardenProperties;
    @Autowired private UserTransaction userTransaction;

    private Boolean userIsInRegistrationPeriod(UserDBO user) {
        return user.getCreationDate().plus(jWardenProperties.getRegistrationTimeout()).isAfter(LocalDateTime.now());
    }

    private void prepareUser(User user) {
        var userDBO = userRepository.findOneByEmail(user.getEmail());
        if (!Objects.isNull(userDBO)) {
            if (userIsInRegistrationPeriod(userDBO) && !userDBO.getEmailVerified()) {
                userTransaction.tryToCreateUser(user);
            } else {
                throw new EmailAlreadyExistsException();
            }
        } else {
            userTransaction.tryToCreateUser(user);
        }
    }

    public User createUser(RegisterUser registerUser) {
        var user = new User(registerUser);
        user.setMasterPasswordSalt(pbkdf2Service.generateSalt());
        user.setMasterPasswordHash(pbkdf2Service.generateSaltedHash(
                registerUser.getMasterPassword(), user.getMasterPasswordSalt()
        ));
        user.setEmailToken(UUID.randomUUID().toString());
        user.setCreationDate(LocalDateTime.now());
        user.setEmailVerified(false);
        prepareUser(user);
        emailService.sendRegisterEmail(user);
        return user;
    }

    public void verifyEmail(String userId, String token) {
        var user = userRepository.findOneById(userId);
        if (user.getCreationDate().plus(jWardenProperties.getRegistrationTimeout()).isBefore(LocalDateTime.now())) {
            throw new EmailVerificationTokenExpiredException();
        }
        if (user.getEmailToken().equals(token)) {
            user.setEmailVerified(true);
            userRepository.save(user);
        } else {
            throw new InvalidEmailVerificationTokenException();
        }
    }

    public void requestEmailChange(String userId, String email) {
        var userDBO = userRepository.findOneById(userId);
        userDBO.setEmailToken(UUID.randomUUID().toString());
        userRepository.save(userDBO);
        emailService.sendEmailChangeEmail(new User(userDBO), email);
    }

    public void emailChange(String userId, String token, String email) {
        var userDBO = userRepository.findOneById(userId);
        if (userDBO.getEmailToken().equals(token)) {
            userDBO.setEmail(email);
            userRepository.save(userDBO);
        } else {
            throw new InvalidEmailVerificationTokenException();
        }
    }
}
