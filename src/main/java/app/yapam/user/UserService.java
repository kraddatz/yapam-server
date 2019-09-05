package app.yapam.user;

import app.yapam.common.error.UnknownUserException;
import app.yapam.common.repository.UserRepository;
import app.yapam.common.service.EmailService;
import app.yapam.common.service.MappingService;
import app.yapam.user.model.request.UserRequest;
import app.yapam.user.model.response.SimpleUserResponse;
import app.yapam.user.model.response.SimpleUserResponseWrapper;
import app.yapam.user.model.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private EmailService emailService;
    @Autowired private MappingService mappingService;

    public UserResponse createUser(UserRequest userRequest) {
        var user = mappingService.userFromRequest(userRequest);
        user.setCreationDate(LocalDateTime.now());
        user.setCreationDate(LocalDateTime.now());
        user.setId(SecurityContextHolder.getContext().getAuthentication().getName());
        var userDao = userRepository.save(mappingService.userToDao(user));
        emailService.sendWelcomeMail(user);
        return mappingService.userDaoToResponse(userDao);
    }

    @PreAuthorize("@permissionEvaluator.registeredUser()")
    public SimpleUserResponseWrapper getAllUsers() {
        var simpleUserResponse = userRepository.findAll().stream().map(user -> mappingService.userDaoToSimpleResponse(user)).collect(Collectors.toSet());
        var simpleUserResponseWrapper = new SimpleUserResponseWrapper();
        simpleUserResponseWrapper.setUsers(simpleUserResponse);
        return simpleUserResponseWrapper;
    }

    @PreAuthorize("@permissionEvaluator.registeredUser()")
    public UserResponse getCurrentUser() {
        var user = userRepository.findOneById(SecurityContextHolder.getContext().getAuthentication().getName());
        return mappingService.userDaoToResponse(user);
    }

    @PreAuthorize("@permissionEvaluator.registeredUser()")
    public SimpleUserResponse getSimpleUserById(String userId) {
        var userDao = userRepository.findOneById(userId);
        if (Objects.isNull(userDao)) {
            throw new UnknownUserException(userId);
        }
        return mappingService.userDaoToSimpleResponse(userDao);
    }
}
