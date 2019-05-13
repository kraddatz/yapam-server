package me.raddatz.jwarden.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("local")
@EnableWebSecurity
public class DefaultSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired private OauthUserDetailsService oauthUserDetailsService;

	@Autowired private PasswordEncoder passwordEncoder;

	@Autowired private AuthenticationProviderConfig authProvider;

	@Override
	public void configure(AuthenticationManagerBuilder auth) {
	auth.authenticationProvider(authProvider);
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(oauthUserDetailsService).passwordEncoder(this.passwordEncoder);
	}

	@Override
	public void configure(WebSecurity web) {
		web
				.ignoring().antMatchers("/documentation/**",
						"/users/*/email/verify",
						"/users/*/email/requestChange",
						"/users/*/email/change").and()
				.ignoring().antMatchers(HttpMethod.POST, "/users");
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}
