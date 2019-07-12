package app.yapam.user;

import app.yapam.common.error.EmailAlreadyExistsException;
import app.yapam.common.error.EmailVerificationTokenExpiredException;
import app.yapam.common.error.InvalidEmailVerificationTokenException;
import app.yapam.common.service.EmailService;
import app.yapam.common.service.MappingService;
import app.yapam.common.service.RequestHelperService;
import app.yapam.config.YapamProperties;
import app.yapam.user.model.request.PasswordChangeRequest;
import app.yapam.user.model.response.SimpleUserResponse;
import app.yapam.user.model.response.UserResponse;
import app.yapam.user.repository.UserDBO;
import app.yapam.user.repository.UserRepository;
import app.yapam.user.repository.UserTransactions;
import app.yapam.user.model.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private EmailService emailService;
    @Autowired private YapamProperties yapamProperties;
    @Autowired private UserTransactions userTransactions;
    @Autowired private RequestHelperService requestHelperService;
    @Autowired private MappingService mappingService;

    public UserResponse getCurrentUser() {
        var user = userRepository.findOneByEmail(requestHelperService.getUserName());
        return mappingService.userDBOToResponse(user);
    }

    public SimpleUserResponse getSimpleUserById(String userId) {
        return mappingService.userDBOToSimpleResponse(userRepository.findOneById(userId));
    }

    @java.lang.SuppressWarnings("squid:S1066")
    private UserDBO checkExistingUser(String email) {
        var userDBO = userRepository.findOneByEmail(email);
        if (Objects.nonNull(userDBO) && userDBO.getEmailVerified()) {
            throw new EmailAlreadyExistsException();
        }
        return Objects.isNull(userDBO) ? new UserDBO() : userDBO;
    }

    public UserResponse createUser(UserRequest userRequest) {
        var userDBO = checkExistingUser(userRequest.getEmail());
        userDBO = mappingService.copyUserRequestToDBO(userRequest, userDBO);
        userDBO.setEmailToken(UUID.randomUUID().toString());
        userDBO.setCreationDate(LocalDateTime.now());
        userDBO.setEmailVerified(false);
        userTransactions.tryToCreateUser(userDBO);
        var user = mappingService.userFromDBO(userDBO);
        emailService.sendRegisterEmail(user);
        return mappingService.userToResponse(user);
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

    public void requestEmailChange(String email) {
        var userDBO = userRepository.findOneByEmail(requestHelperService.getUserName());
        userDBO.setEmailToken(UUID.randomUUID().toString());
        userRepository.save(userDBO);
        emailService.sendEmailChangeEmail(mappingService.userFromDBO(userDBO), email);
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

    public void passwordChange(PasswordChangeRequest passwordChangeRequest) {
        var userDBO = userRepository.findOneByEmail(requestHelperService.getUserName());
        userDBO.setMasterPasswordHash(passwordChangeRequest.getMasterPasswordHash());
        userDBO.setMasterPasswordHint(passwordChangeRequest.getMasterPasswordHint());
        userTransactions.tryToUpdateUser(userDBO);
    }

    public Set<SimpleUserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(user -> mappingService.userDBOToSimpleResponse(user)).collect(Collectors.toSet());
    }
}
