package me.raddatz.yapam.common.service;

import me.raddatz.yapam.user.repository.UserDBO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BCryptService.class)
@ActiveProfiles("test")
class BCryptServiceTest {

    @Autowired private BCryptService bcryptService;

    @Test
    void test_generateSaltedHash() {
        var saltedHashedPassword = bcryptService.generateSaltedHash("MasterPassword123");

        assertNotNull(saltedHashedPassword);
    }

    @Test
    void equals_whenPasswordIsValid_thenReturnTrue() {
        var user = new UserDBO();
        user.setMasterPasswordHash(bcryptService.generateSaltedHash("MasterPassword123"));

        var result = bcryptService.checkHash("MasterPassword123", user);
        assertTrue(result);
    }

    @Test
    void equals_whenPasswordIsInvalid_thenReturnFalse() {
        var user = new UserDBO();
        user.setMasterPasswordHash(bcryptService.generateSaltedHash("MasterPassword123"));

        var result = bcryptService.checkHash("wrong_password", user);
        assertFalse(result);
    }
}