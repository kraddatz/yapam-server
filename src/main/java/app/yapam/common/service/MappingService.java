package app.yapam.common.service;

import app.yapam.common.repository.SecretDao;
import app.yapam.common.repository.UserDao;
import app.yapam.common.repository.UserRepository;
import app.yapam.common.repository.UserSecretDao;
import app.yapam.secret.model.Secret;
import app.yapam.secret.model.UserSecretPrivilege;
import app.yapam.secret.model.request.SecretRequest;
import app.yapam.secret.model.request.UserIdSecretPrivilege;
import app.yapam.secret.model.response.SecretResponse;
import app.yapam.secret.model.response.SimpleSecretResponse;
import app.yapam.secret.model.response.SimpleUserPrivilegeResponse;
import app.yapam.user.model.User;
import app.yapam.user.model.request.UserRequest;
import app.yapam.user.model.response.SimpleUserResponse;
import app.yapam.user.model.response.UserResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MappingService {

    @Autowired private UserRepository userRepository;
    @Autowired private RequestHelperService requestHelperService;

    public SecretResponse secretDaoToResponse(SecretDao secretDao) {
        return secretToResponse(secretFromDao(secretDao));
    }

    public SimpleSecretResponse secretDaoToSimpleResponse(SecretDao secretDao) {
        return secretToSimpleResponse(secretFromDao(secretDao));
    }

    public Secret secretFromDao(SecretDao secretDao) {
        var secret = new Secret();
        BeanUtils.copyProperties(secretDao, secret);
        List<UserSecretPrivilege> userSecretPrivileges = new ArrayList<>();
        for (UserSecretDao userSecret : secretDao.getUsers()) {
            var user = userFromDao(userSecret.getUser());
            var userSecretPrivilege = new UserSecretPrivilege(user, userSecret.getPrivileged());
            userSecretPrivileges.add(userSecretPrivilege);
        }
        secret.setUsers(userSecretPrivileges);
        return secret;
    }

    public Secret secretFromRequest(SecretRequest secretRequest) {
        var secret = new Secret();
        BeanUtils.copyProperties(secretRequest, secret);
        List<UserSecretPrivilege> users = new ArrayList<>();
        for (UserIdSecretPrivilege userId : secretRequest.getUsers()) {
            var user = userFromDao(userRepository.findOneById(userId.getUserId()));
            var privilege = new UserSecretPrivilege(user, userId.getPrivileged());
            users.add(privilege);
        }
        secret.setUsers(users);
        return secret;
    }

    public SecretDao secretToDao(Secret secret) {
        var secretDao = new SecretDao();
        BeanUtils.copyProperties(secret, secretDao, "id");
        Set<UserSecretDao> userSecrets = new HashSet<>();
        for (UserSecretPrivilege userSecretPrivilege : secret.getUsers()) {
            userSecrets.add(new UserSecretDao(secretDao, userToDao(userSecretPrivilege.getUser()), userSecretPrivilege.getPrivilege()));
        }
        secretDao.setUsers(userSecrets);
        return secretDao;
    }

    public SecretResponse secretToResponse(Secret secret) {
        var secretResponse = new SecretResponse();
        BeanUtils.copyProperties(secret, secretResponse);
        List<SimpleUserPrivilegeResponse> users = new ArrayList<>();
        for (UserSecretPrivilege userSecretPrivilege : secret.getUsers()) {
            var user = userToSimpleResponse(userSecretPrivilege.getUser());
            var simpleUserPrivilegeResponse = new SimpleUserPrivilegeResponse(user, userSecretPrivilege.getPrivilege());
            users.add(simpleUserPrivilegeResponse);
        }
        secretResponse.setUsers(users);
        return secretResponse;
    }

    public SimpleSecretResponse secretToSimpleResponse(Secret secret) {
        var simpleSecretResponse = new SimpleSecretResponse();
        BeanUtils.copyProperties(secret, simpleSecretResponse);
        return simpleSecretResponse;
    }

    public UserResponse userDaoToResponse(UserDao user) {
        return userToResponse(userFromDao(user));
    }

    public SimpleUserResponse userDaoToSimpleResponse(UserDao userDao) {
        return userToSimpleResponse(userFromDao(userDao));
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
}
