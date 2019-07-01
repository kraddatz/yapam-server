package app.yapam.config.oauth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

class OauthUserDetails extends User {

	OauthUserDetails(String userName, String password, Collection<? extends GrantedAuthority> authorities) {
		super(userName, password, authorities);
	}

}
