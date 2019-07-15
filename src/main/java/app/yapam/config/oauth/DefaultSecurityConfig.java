package app.yapam.config.oauth;

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
@Profile("!test")
@EnableWebSecurity
public class DefaultSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired private OauthUserDetailsService oauthUserDetailsService;

    @Autowired private PasswordEncoder passwordEncoder;

    @Autowired private CentralIdentityAuthenticator authProvider;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring().antMatchers("/documentation/**",
                "/api/users/**/email/verify",
                "/api/users/**/email/change",
                "/actuator/**",
                "/api/status",
                "/api/health")
                .and()
                .ignoring().antMatchers(HttpMethod.POST, "/api/users");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(oauthUserDetailsService).passwordEncoder(this.passwordEncoder);
    }

}
