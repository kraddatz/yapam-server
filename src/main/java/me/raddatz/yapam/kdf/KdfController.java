package me.raddatz.yapam.kdf;

import me.raddatz.yapam.kdf.model.response.KdfInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KdfController {

    @Autowired private KdfService kdfService;

    @GetMapping(value = "/api/kdf")
    public KdfInfo getKdfInfo() {
        return kdfService.getKdfInfo();
    }
}
