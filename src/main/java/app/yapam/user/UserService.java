package app.yapam.user;

import app.yapam.common.repository.UserRepository;
import app.yapam.common.service.EmailService;
import app.yapam.common.service.MappingService;
import app.yapam.common.service.RequestHelperService;
import app.yapam.user.model.request.UserRequest;
import app.yapam.user.model.response.SimpleUserResponse;
import app.yapam.user.model.response.SimpleUserResponseWrapper;
import app.yapam.user.model.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private EmailService emailService;
    @Autowired private RequestHelperService requestHelperService;
    @Autowired private MappingService mappingService;

    public UserResponse createUser(UserRequest userRequest) {
        var user = mappingService.userFromRequest(userRequest);
        user.setCreationDate(LocalDateTime.now());
        user.setEmail(requestHelperService.getEmail());
        user.setCreationDate(LocalDateTime.now());
        userRepository.save(mappingService.userToDao(user));
        emailService.sendWelcomeMail(user);
        return mappingService.userToResponse(user);
    }

    public SimpleUserResponseWrapper getAllUsers() {
        var simpleUserResponse = userRepository.findAll().stream().map(user -> mappingService.userDaoToSimpleResponse(user)).collect(Collectors.toSet());
        var simpleUserResponseWrapper = new SimpleUserResponseWrapper();
        simpleUserResponseWrapper.setUsers(simpleUserResponse);
        return simpleUserResponseWrapper;
    }

    public UserResponse getCurrentUser() {
        var user = userRepository.findOneByEmail(requestHelperService.getEmail());
        return mappingService.userDaoToResponse(user);
    }

    public SimpleUserResponse getSimpleUserById(String userId) {
        return mappingService.userDaoToSimpleResponse(userRepository.findOneById(userId));
    }
}
