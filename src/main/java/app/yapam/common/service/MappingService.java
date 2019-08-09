package app.yapam.common.service;

import app.yapam.secret.model.Secret;
import app.yapam.secret.model.request.SecretRequest;
import app.yapam.secret.model.response.SecretResponse;
import app.yapam.secret.model.response.SimpleSecretResponse;
import app.yapam.secret.repository.SecretDao;
import app.yapam.user.model.User;
import app.yapam.user.model.request.UserRequest;
import app.yapam.user.model.response.SimpleUserResponse;
import app.yapam.user.model.response.UserResponse;
import app.yapam.user.repository.UserDao;
import app.yapam.user.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MappingService {

    @Autowired private UserRepository userRepository;
    @Autowired private RequestHelperService requestHelperService;

    public Secret secretFromDao(SecretDao secretDao) {
        var secret = new Secret();
        BeanUtils.copyProperties(secretDao, secret);
        if (!Objects.isNull(secretDao.getUser())) {
            secret.setUser(userFromDao(secretDao.getUser()));
        }
        return secret;
    }

    public SecretDao secretToDao(Secret secret) {
        var secretDBO = new SecretDao();
        BeanUtils.copyProperties(secret, secretDBO, "id");
        if (!Objects.isNull(secret.getUser())) {
            secretDBO.setUser(userToDao(secret.getUser()));
        }
        return secretDBO;
    }

    public Secret secretFromRequest(SecretRequest secretRequest) {
        var secret = new Secret();
        BeanUtils.copyProperties(secretRequest, secret);
        if (!Objects.isNull(secretRequest.getUserId())) {
            secret.setUser(userFromDao(userRepository.findOneById(secretRequest.getUserId())));
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

    public SimpleSecretResponse secretToSimpleResponse(Secret secret) {
        var simpleSecretResponse = new SimpleSecretResponse();
        BeanUtils.copyProperties(secret, simpleSecretResponse);
        return simpleSecretResponse;
    }

    public SecretResponse secretDaoToResponse(SecretDao secretDao) {
        return secretToResponse(secretFromDao(secretDao));
    }

    public SimpleSecretResponse secretDaoToSimpleResponse(SecretDao secretDao) {
        return secretToSimpleResponse(secretFromDao(secretDao));
    }

    public UserResponse userDaoToResponse(UserDao user) {
        return userToResponse(userFromDao(user));
    }

    public User userFromDao(UserDao userDao) {
        var user = new User();
        BeanUtils.copyProperties(userDao, user);
        return user;
    }

    public User userFromRequest(UserRequest userRequest) {
        var user = new User();
        BeanUtils.copyProperties(userRequest, user);
        user.setEmail(requestHelperService.getEmail());
        return user;
    }

    public UserDao userToDao(User user) {
        var userDBO = new UserDao();
        BeanUtils.copyProperties(user, userDBO);
        return userDBO;
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

    public SimpleUserResponse userDaoToSimpleResponse(UserDao userDao) {
        return userToSimpleResponse(userFromDao(userDao));
    }
}
