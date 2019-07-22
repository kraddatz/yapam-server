package app.yapam.user.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SimpleUserResponseWrapper {

    private Set<SimpleUserResponse> users;
}
