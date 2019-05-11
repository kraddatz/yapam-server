package me.raddatz.jwarden.user;

import me.raddatz.jwarden.common.error.EmailAlreadyExistsException;
import me.raddatz.jwarden.common.error.InvalidEmailVerificationTokenException;
import me.raddatz.jwarden.common.service.EmailService;
import me.raddatz.jwarden.common.service.PBKDF2Service;
import me.raddatz.jwarden.config.JWardenConfig;
import me.raddatz.jwarden.user.model.RegisterUser;
import me.raddatz.jwarden.user.model.User;
import me.raddatz.jwarden.user.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private EmailService emailService;
    @Autowired private PBKDF2Service pbkdf2Service;
    @Autowired private JWardenConfig jWardenConfig;

    private Boolean userIsInRegistrationPeriod(User user) {
        return user.getCreationDate().plus(jWardenConfig.getRegistrationTimeout()).isAfter(LocalDateTime.now());
    }

    @Transactional
    void tryToRegisterUser(User user) {
        var dbUser = userRepository.findOneByEmail(user.getEmail());
        if (!Objects.isNull(dbUser)) {
            if (!(userIsInRegistrationPeriod(dbUser) && !dbUser.getEmailVerified())) {
                throw new EmailAlreadyExistsException();
            }
            BeanUtils.copyProperties(user, dbUser, "id");
            userRepository.save(dbUser);
        } else {
            userRepository.save(user);
        }
    }

    public RegisterUser register(RegisterUser registerUser) {
        var user = registerUser.toUser();
        user.setMasterPasswordSalt(pbkdf2Service.generateSalt());
        user.setMasterPasswordHash(pbkdf2Service.generateSaltedHash(
                registerUser.getMasterPassword(), user.getMasterPasswordSalt()
        ));
        user.setEmailToken(UUID.randomUUID().toString());
        user.setCreationDate(LocalDateTime.now());
        user.setEmailVerified(false);
        tryToRegisterUser(user);
        emailService.sendRegisterEmail(user);
        return registerUser;
    }

    @Transactional
    public void verifyEmail(String userId, String token) {
        var user = userRepository.findOneById(userId);
        if (user.getEmailToken().equals(token)) {
            user.setEmailVerified(true);
            userRepository.save(user);
        } else {
            throw new InvalidEmailVerificationTokenException();
        }
    }

    @Transactional
    public void requestEmailChange(String userId, String email) {
        var user = userRepository.findOneById(userId);
        user.setEmailToken(UUID.randomUUID().toString());
        userRepository.save(user);
        emailService.sendEmailChangeEmail(user, email);
    }

    @Transactional
    public void emailChange(String userId, String token, String email) {
        var user = userRepository.findOneById(userId);
        if (user.getEmailToken().equals(token)) {
            user.setEmail(email);
            userRepository.save(user);
        } else {
            throw new InvalidEmailVerificationTokenException();
        }
    }
}
