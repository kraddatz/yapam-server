package me.raddatz.yapam.config.oauth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class OauthUserDetails extends User {

	public OauthUserDetails(String userName, String password, Collection<? extends GrantedAuthority> authorities) {
		super(userName, password, authorities);
	}

}
