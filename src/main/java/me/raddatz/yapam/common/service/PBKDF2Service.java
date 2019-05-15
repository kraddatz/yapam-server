package me.raddatz.yapam.common.service;

import lombok.extern.slf4j.Slf4j;
import me.raddatz.yapam.common.error.InternalServerError;
import me.raddatz.yapam.user.repository.UserDBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Service
@Slf4j
public class PBKDF2Service {

    @Autowired private SecretKeyFactory secretKeyFactory;

    public String generateSalt() {
        byte[] salt = new byte[64];
        new SecureRandom().nextBytes(salt );
        return Base64.getEncoder().encodeToString(salt);
    }

    public String generateSaltedHash(String password, String salt) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 100000, 512);
        try {
            var secret = secretKeyFactory.generateSecret(spec);
            return Base64.getEncoder().encodeToString(secret.getEncoded());
        } catch (InvalidKeySpecException e) {
            log.error(e.getMessage(), e);
        }
        throw new InternalServerError();
    }

    public boolean equals(String password, String salt, UserDBO user) {
        var saltedHash = generateSaltedHash(password, salt);
        return saltedHash.equals(user.getMasterPasswordHash());
    }

}
