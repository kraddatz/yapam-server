package me.raddatz.yapam.secret.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class SecretTransactions {

    @Autowired private SecretRepository secretRepository;

    @Transactional(rollbackOn = RuntimeException.class)
    public void saveSecret(SecretDBO secretDBO) {
        secretRepository.save(secretDBO);
    }
}
