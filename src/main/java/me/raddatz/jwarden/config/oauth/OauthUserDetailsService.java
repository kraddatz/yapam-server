package me.raddatz.jwarden.config.oauth;

import me.raddatz.jwarden.common.error.UserNotFoundException;
import me.raddatz.jwarden.user.model.Role;
import me.raddatz.jwarden.user.model.User;
import me.raddatz.jwarden.user.repository.RoleRepository;
import me.raddatz.jwarden.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OauthUserDetailsService implements UserDetailsService {

	@Autowired private UserRepository userRepository;
	@Autowired private RoleRepository roleRepository;

	@Override
	public UserDetails loadUserByUsername(String email) {
		User user = userRepository.findOneByEmail(email);
		if (null == user) {
			throw new UserNotFoundException();
		} else {

			List<Role> userRoles = roleRepository.findByUser(user);

			ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
			if (userRoles != null && !userRoles.isEmpty()) {
				for (Role role : userRoles) {
					grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
				}
			}

			return new OauthUserDetails(user.getEmail(), user.getMasterPasswordHash(), grantedAuthorities);
		}
	}
}
