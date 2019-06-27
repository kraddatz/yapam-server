package app.yapam.common.service;

import app.yapam.secret.model.Secret;
import app.yapam.secret.model.request.SecretRequest;
import app.yapam.secret.model.response.SecretResponse;
import app.yapam.secret.repository.SecretDBO;
import app.yapam.user.model.User;
import app.yapam.user.model.request.UserRequest;
import app.yapam.user.model.response.SimpleUserResponse;
import app.yapam.user.model.response.UserResponse;
import app.yapam.user.repository.UserDBO;
import app.yapam.user.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MappingService {

    @Autowired private UserRepository userRepository;

    public Secret secretFromDBO(SecretDBO secretDBO) {
        var secret = new Secret();
        BeanUtils.copyProperties(secretDBO, secret);
        if (!Objects.isNull(secretDBO.getUser())) {
            secret.setUser(userFromDBO(secretDBO.getUser()));
        }
        return secret;
    }

    public SecretDBO secretToDBO(Secret secret) {
        var secretDBO = new SecretDBO();
        BeanUtils.copyProperties(secret, secretDBO, "id");
        if (!Objects.isNull(secret.getUser())) {
            secretDBO.setUser(userToDBO(secret.getUser()));
        }
        return secretDBO;
    }

    public Secret secretFromRequest(SecretRequest secretRequest) {
        var secret = new Secret();
        BeanUtils.copyProperties(secretRequest, secret);
        if (!Objects.isNull(secretRequest.getUserId())) {
            secret.setUser(userFromDBO(userRepository.findOneById(secretRequest.getUserId())));
        }
        return secret;
    }

    public SecretResponse secretToResponse(Secret secret) {
        var secretResponse = new SecretResponse();
        BeanUtils.copyProperties(secret, secretResponse);
        if (!Objects.isNull(secret.getUser())) {
            secretResponse.setUser(userToSimpleResponse(secret.getUser()));
        }
        return secretResponse;
    }

    public SecretResponse secretDBOToResponse(SecretDBO secretDBO) {
        return secretToResponse(secretFromDBO(secretDBO));
    }

    public UserResponse userDBOToResponse(UserDBO user) {
        return userToResponse(userFromDBO(user));
    }

    public User userFromDBO(UserDBO userDBO) {
        var user = new User();
        BeanUtils.copyProperties(userDBO, user);
        return user;
    }

    public User userFromRequest(UserRequest userRequest) {
        var user = new User();
        BeanUtils.copyProperties(userRequest, user);
        return user;
    }

    public UserDBO userToDBO(User user) {
        var userDBO = new UserDBO();
        BeanUtils.copyProperties(user, userDBO);
        return userDBO;
    }

    public UserDBO copyUserRequestToDBO(UserRequest from, UserDBO to) {
        BeanUtils.copyProperties(from, to, "id", "emailVerified", "emailToken", "creationDate");
        return to;
    }

    public UserResponse userToResponse(User user) {
        var userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        return userResponse;
    }

    private SimpleUserResponse userToSimpleResponse(User user) {
        var simpleUserResponse = new SimpleUserResponse();
        BeanUtils.copyProperties(user, simpleUserResponse);
        return simpleUserResponse;
    }

    public SimpleUserResponse userDBOToSimpleResponse(UserDBO userDBO) {
        return userToSimpleResponse(userFromDBO(userDBO));
    }
}
