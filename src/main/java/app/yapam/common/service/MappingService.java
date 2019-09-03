package app.yapam.common.service;

import app.yapam.common.error.InvalidFileContentException;
import app.yapam.common.repository.*;
import app.yapam.file.model.File;
import app.yapam.file.model.response.FileResponse;
import app.yapam.file.model.response.SimpleFileResponse;
import app.yapam.secret.model.Secret;
import app.yapam.secret.model.UserSecretPrivilege;
import app.yapam.secret.model.request.SecretRequest;
import app.yapam.secret.model.request.UserIdSecretPrivilege;
import app.yapam.secret.model.response.SecretResponse;
import app.yapam.secret.model.response.SimpleSecretResponse;
import app.yapam.secret.model.response.SimpleUserPrivilegeResponse;
import app.yapam.tag.model.Tag;
import app.yapam.tag.model.response.TagResponse;
import app.yapam.user.model.User;
import app.yapam.user.model.request.UserRequest;
import app.yapam.user.model.response.SimpleUserResponse;
import app.yapam.user.model.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
public class MappingService {

    @Autowired private UserRepository userRepository;
    @Autowired private RequestHelperService requestHelperService;
    @Autowired private FileRepository fileRepository;
    @Autowired private TagRepository tagRepository;

    public SimpleFileResponse fileDaoToSimpleResponse(FileDao fileDao) {
        return fileToSimpleResponse(fileFromDao(fileDao));
    }

    public File fileFromDao(FileDao fileDao) {
        var file = new File();
        BeanUtils.copyProperties(fileDao, file);
        return file;
    }

    public File fileFromRequest(MultipartFile multipartFile) {
        var file = new File();
        file.setFilename(multipartFile.getOriginalFilename());
        file.setFilesize(multipartFile.getSize());
        try {
            file.setHash(new String(Base64.getEncoder().encode(MessageDigest.getInstance("SHA-1").digest(multipartFile.getBytes()))));
            file.setContent(multipartFile.getBytes());
        } catch (IOException | NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
            throw new InvalidFileContentException();
        }
        return file;
    }

    public FileDao fileToDao(File file) {
        var fileDao = new FileDao();
        BeanUtils.copyProperties(file, fileDao);
        return fileDao;
    }

    public FileResponse fileToResponse(File file) {
        var fileResponse = new FileResponse();
        BeanUtils.copyProperties(file, fileResponse);
        return fileResponse;
    }

    public SimpleFileResponse fileToSimpleResponse(File file) {
        var simpleFileResponse = new SimpleFileResponse();
        BeanUtils.copyProperties(file, simpleFileResponse);
        return simpleFileResponse;
    }

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

        List<File> files = new ArrayList<>();
        for (FileDao fileDao : secretDao.getFiles()) {
            files.add(fileFromDao(fileDao));
        }
        secret.setFiles(files);

        List<Tag> tags = new ArrayList<>();
        for (TagDao tagDao : secretDao.getTags()) {
            tags.add(tagFromDao(tagDao));
        }
        secret.setTags(tags);

        return secret;
    }

    public Secret secretFromRequest(SecretRequest secretRequest) {
        var secret = new Secret();
        BeanUtils.copyProperties(secretRequest, secret);
        List<UserSecretPrivilege> users = new ArrayList<>();
        for (UserIdSecretPrivilege userIdSecretPrivilege : secretRequest.getUsers()) {
            var user = userFromDao(userRepository.findOneById(userIdSecretPrivilege.getUserId()));
            var privilege = new UserSecretPrivilege(user, userIdSecretPrivilege.getPrivileged());
            users.add(privilege);
        }
        secret.setUsers(users);

        List<File> files = new ArrayList<>();
        for (String fileId : secretRequest.getFiles()) {
            files.add(fileFromDao(fileRepository.findOneById(fileId)));
        }
        secret.setFiles(files);

        List<Tag> tags = new ArrayList<>();
        for (String tagId : secretRequest.getTags()) {
            tags.add(tagFromDao(tagRepository.findOneById(tagId)));
        }
        secret.setTags(tags);

        return secret;
    }

    public SecretDao secretToDao(Secret secret) {
        var secretDao = new SecretDao();
        BeanUtils.copyProperties(secret, secretDao, "id");
        List<UserSecretDao> userSecrets = new ArrayList<>();
        for (UserSecretPrivilege userSecretPrivilege : secret.getUsers()) {
            userSecrets.add(new UserSecretDao(secretDao, userToDao(userSecretPrivilege.getUser()), userSecretPrivilege.getPrivilege()));
        }
        secretDao.setUsers(userSecrets);

        List<TagDao> tags = new ArrayList<>();
        for (Tag tag : secret.getTags()) {
            tags.add(tagToDao(tag));
        }
        secretDao.setTags(tags);

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

        List<SimpleFileResponse> files = new ArrayList<>();
        for (File file : secret.getFiles()) {
            files.add(fileToSimpleResponse(file));
        }
        secretResponse.setFiles(files);

        List<String> tags = new ArrayList<>();
        for (Tag tag : secret.getTags()) {
            tags.add(tag.getName());
        }
        secretResponse.setTags(tags);

        return secretResponse;
    }

    public SimpleSecretResponse secretToSimpleResponse(Secret secret) {
        var simpleSecretResponse = new SimpleSecretResponse();
        BeanUtils.copyProperties(secret, simpleSecretResponse);
        return simpleSecretResponse;
    }

    public TagResponse tagDaoToResponse(TagDao tagDao) {
        var tagResponse = new TagResponse();
        BeanUtils.copyProperties(tagDao, tagResponse);
        return tagResponse;
    }

    public Tag tagFromDao(TagDao tagDao) {
        var tag = new Tag();
        BeanUtils.copyProperties(tagDao, tag);
        return tag;
    }

    public TagDao tagToDao(Tag tag) {
        var tagDao = new TagDao();
        BeanUtils.copyProperties(tag, tagDao);
        return tagDao;
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
