package app.yapam.config;

import app.yapam.common.repository.UserDao;
import app.yapam.common.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.util.Optional;

@Configuration
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JPAConfig {

    @Bean
    AuditorAware<UserDao> auditorProvider(UserRepository userRepository) {

        return new AuditorAware<UserDao>() {
            @Override
            public Optional<UserDao> getCurrentAuditor() {
                var userId = SecurityContextHolder.getContext().getAuthentication().getName();
                return Optional.of(userRepository.findOneById(userId));
            }
        };
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
