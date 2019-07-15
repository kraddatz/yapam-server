package app.yapam.kdf.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KdfInfo {

    @ApiModelProperty(value = "The current hash iterations the administrator enforces", example = "10")
    private Integer iterations;
    @ApiModelProperty(value = "Indicator if the hash iterations match the administrators enforcement")
    private Boolean secure;
}
