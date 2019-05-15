package me.raddatz.yapam.common.service;

import me.raddatz.yapam.config.PBDKF2Config;
import me.raddatz.yapam.user.repository.UserDBO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PBKDF2Service.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {PBKDF2Service.class, PBDKF2Config.class})
class PBKDF2ServiceTest {

    @Autowired private PBKDF2Service pbkdf2Service;

    @Test
    void generateSaltedHash_whenValidKeySpec_thenReturnSaltedHash() {
        var salt = pbkdf2Service.generateSalt();
        var password = "password";
        var saltedHashedPassword = pbkdf2Service.generateSaltedHash(password, salt);

        assertNotNull(saltedHashedPassword);
    }

    @Test
    void equals_whenPasswordIsInvalid_thenReturnTrue() {
        var salt = pbkdf2Service.generateSalt();
        var user = new UserDBO();
        user.setMasterPasswordSalt(salt);
        user.setMasterPasswordHash(pbkdf2Service.generateSaltedHash("password", salt));

        var result = pbkdf2Service.equals("password", salt, user);
        assertTrue(result);
    }

    @Test
    void equals_whenPasswordIsInvalid_thenReturnFalse() {
        var salt = pbkdf2Service.generateSalt();
        var user = new UserDBO();
        user.setMasterPasswordSalt(salt);
        user.setMasterPasswordHash(pbkdf2Service.generateSaltedHash("password", salt));
        user.setMasterPasswordHash(pbkdf2Service.generateSaltedHash("password", salt));

        var result = pbkdf2Service.equals("wrong_password", salt, user);
        assertFalse(result);
    }
}