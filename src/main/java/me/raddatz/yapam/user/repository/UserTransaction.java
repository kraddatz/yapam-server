package me.raddatz.yapam.user.repository;

import me.raddatz.yapam.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class UserTransaction {

    @Autowired private UserRepository userRepository;

    @Transactional
    public void tryToCreateUser(User user) {
        var userDBO = user.toDBO();
        userRepository.save(userDBO);
    }
}
