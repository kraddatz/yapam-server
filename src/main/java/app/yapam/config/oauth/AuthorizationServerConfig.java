package app.yapam.config.oauth;

import app.yapam.config.YapamProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private YapamProperties yapamProperties;
    @Autowired private JwtAccessTokenConverter accessTokenConverter;
    @Autowired private TokenStore tokenStore;

    @Override
    @java.lang.SuppressWarnings("squid:S2068")
    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {

        configurer//.withClientDetails(clientDetailsService)
                .inMemory()
                .withClient("yapam")
                .secret(passwordEncoder.encode(""))
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read", "write")
                .accessTokenValiditySeconds((int) yapamProperties.getSecurity().getOauth().getAccessTokenValidityPeriod().toSeconds())
                .refreshTokenValiditySeconds((int) yapamProperties.getSecurity().getOauth().getRefreshTokenValidityPeriod().toSeconds());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(tokenStore)
                .reuseRefreshTokens(false)
                .authenticationManager(authenticationManager)
                .accessTokenConverter(accessTokenConverter);
    }
}
