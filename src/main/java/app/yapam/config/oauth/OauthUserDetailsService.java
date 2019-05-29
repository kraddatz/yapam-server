package app.yapam.config.oauth;

import app.yapam.common.error.UserNotFoundException;
import app.yapam.user.repository.RoleDBO;
import app.yapam.user.repository.UserDBO;
import app.yapam.user.repository.RoleRepository;
import app.yapam.user.repository.UserRepository;
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
		UserDBO user = userRepository.findOneByEmail(email);
		if (null == user) {
			throw new UserNotFoundException();
		} else {

			List<RoleDBO> roleDBOs = roleRepository.findByUser(user);

			ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
			if (roleDBOs != null && !roleDBOs.isEmpty()) {
				for (RoleDBO roleDBO : roleDBOs) {
					grantedAuthorities.add(new SimpleGrantedAuthority(roleDBO.getName()));
				}
			}

			return new OauthUserDetails(user.getEmail(), user.getMasterPasswordHash(), grantedAuthorities);
		}
	}
}
