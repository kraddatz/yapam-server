package me.raddatz.yapam.kdf;

import me.raddatz.yapam.common.service.MappingService;
import me.raddatz.yapam.common.service.RequestHelperService;
import me.raddatz.yapam.config.YapamProperties;
import me.raddatz.yapam.kdf.model.response.KdfInfo;
import me.raddatz.yapam.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KdfService {

    @Autowired private YapamProperties yapamProperties;
    @Autowired private UserRepository userRepository;
    @Autowired private MappingService mappingService;
    @Autowired private RequestHelperService requestHelperService;

    private Integer getKdfIterations() {
        return yapamProperties.getSecurity().getBcryptIterations();
    }

    public KdfInfo getKdfInfo() {
        KdfInfo kdfInfo = new KdfInfo();
        var kdfIterations = getKdfIterations();
        kdfInfo.setIterations(kdfIterations);
        var user = mappingService.userFromDBO(userRepository.findOneByEmail(requestHelperService.getUserName()));
        var currentKdfIterations = user.getMasterPasswordHash().split("\\$")[2];
        kdfInfo.setSecure(Integer.valueOf(currentKdfIterations).equals(kdfIterations));
        return kdfInfo;
    }
}
