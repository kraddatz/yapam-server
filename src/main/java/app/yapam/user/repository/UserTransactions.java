package app.yapam.user.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class UserTransactions {

    @Autowired private UserRepository userRepository;

    @Transactional
    public void tryToCreateUser(UserDBO userDBO) {
        userRepository.save(userDBO);
    }

    @Transactional
    public void tryToUpdateUser(UserDBO userDBO) {
        userRepository.save(userDBO);
    }
}
