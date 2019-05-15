package me.raddatz.yapam.user;

import me.raddatz.yapam.common.error.EmailAlreadyExistsException;
import me.raddatz.yapam.common.error.EmailVerificationTokenExpiredException;
import me.raddatz.yapam.common.error.InvalidEmailVerificationTokenException;
import me.raddatz.yapam.common.service.EmailService;
import me.raddatz.yapam.config.YapamProperties;
import me.raddatz.yapam.user.model.RegisterUser;
import me.raddatz.yapam.user.model.User;
import me.raddatz.yapam.user.repository.UserDBO;
import me.raddatz.yapam.user.repository.UserRepository;
import me.raddatz.yapam.user.repository.UserTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private EmailService emailService;
    @Autowired private YapamProperties yapamProperties;
    @Autowired private UserTransaction userTransaction;

    private Boolean userIsInRegistrationPeriod(UserDBO user) {
        return user.getCreationDate().plus(yapamProperties.getRegistrationTimeout()).isAfter(LocalDateTime.now());
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
        user.setEmailToken(UUID.randomUUID().toString());
        user.setCreationDate(LocalDateTime.now());
        user.setEmailVerified(false);
        prepareUser(user);
        emailService.sendRegisterEmail(user);
        return user;
    }

    public void verifyEmail(String userId, String token) {
        var user = userRepository.findOneById(userId);
        if (user.getCreationDate().plus(yapamProperties.getRegistrationTimeout()).isBefore(LocalDateTime.now())) {
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
