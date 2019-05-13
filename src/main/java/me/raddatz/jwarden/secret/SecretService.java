package me.raddatz.jwarden.secret;

import me.raddatz.jwarden.common.service.RequestHelperService;
import me.raddatz.jwarden.secret.model.Secret;
import me.raddatz.jwarden.secret.repository.SecretRepository;
import me.raddatz.jwarden.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecretService {

    @Autowired private SecretRepository secretRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private RequestHelperService requestHelperService;

    Secret createSecret(Secret secret) {
        secret.setUser(userRepository.findOneByEmail(requestHelperService.getUserName()));
        secretRepository.save(secret);
        return secret;
    }
}
