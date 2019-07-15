package app.yapam.kdf;

import app.yapam.kdf.model.response.KdfInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KdfController {

    @Autowired private KdfService kdfService;

    @ApiOperation(value = "Return information about the currently enforced key deriving function")
    @GetMapping(value = "/api/kdf", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public KdfInfo getKdfInfo() {
        return kdfService.getKdfInfo();
    }
}
