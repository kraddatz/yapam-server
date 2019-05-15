package me.raddatz.yapam.secret.repository;

import me.raddatz.yapam.secret.model.Secret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class SecretTransaction {

    @Autowired private SecretRepository secretRepository;

    @Transactional(rollbackOn = RuntimeException.class)
    public void saveSecret(Secret secret) {
        var secretDBO = secret.toDBO();
        secretRepository.save(secretDBO);
    }
}
