package me.raddatz.yapam.common.service;

import lombok.extern.slf4j.Slf4j;
import me.raddatz.yapam.user.repository.UserDBO;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BCryptService {

    public boolean checkHash(String password, UserDBO user) {
        return BCrypt.checkpw(password, user.getMasterPasswordHash());
    }

}
