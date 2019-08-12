package app.yapam.user;

import app.yapam.common.service.EmailService;
import app.yapam.common.service.MappingService;
import app.yapam.common.service.RequestHelperService;
import app.yapam.user.model.response.SimpleUserResponse;
import app.yapam.user.model.response.SimpleUserResponseWrapper;
import app.yapam.user.model.response.UserResponse;
import app.yapam.user.repository.UserDao;
import app.yapam.user.repository.UserRepository;
import app.yapam.user.model.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private EmailService emailService;
    @Autowired private RequestHelperService requestHelperService;
    @Autowired private MappingService mappingService;

    public Mono<UserResponse> getCurrentUser() {
        return userRepository.findOneByEmail(requestHelperService.getEmail())
                .map(u -> mappingService.userDaoToResponse(u));
    }

    public Mono<SimpleUserResponse> getSimpleUserById(String userId) {
        return userRepository.findOneById(userId)
                .map(u -> mappingService.userDaoToSimpleResponse(u));
    }

    public Mono<UserResponse> createUser(UserRequest userRequest) {
        var user = mappingService.userFromRequest(userRequest);
        user.setCreationDate(LocalDateTime.now());
        user.setEmail(requestHelperService.getEmail());
        user.setCreationDate(LocalDateTime.now());
        return userRepository.save(mappingService.userToDao(user))
                .compose(u -> emailService.sendWelcomeMail(mappingService.userFromDao(u)));
                .map(u -> mappingService.userToResponse(u));
        emailService.sendWelcomeMail(user);
        return mappingService.userToResponse(user);
    }

    public SimpleUserResponseWrapper getAllUsers() {
        var simpleUserResponse = userRepository.findAll().stream().map(user -> mappingService.userDaoToSimpleResponse(user)).collect(Collectors.toSet());
        var simpleUserResponseWrapper = new SimpleUserResponseWrapper();
        simpleUserResponseWrapper.setUsers(simpleUserResponse);
        return simpleUserResponseWrapper;
    }
}
