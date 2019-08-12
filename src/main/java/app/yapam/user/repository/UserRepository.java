package app.yapam.user.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<UserDao, String> {

    Mono<UserDao> findOneByEmail(String email);

    Mono<UserDao> findOneById(String userId);
}
