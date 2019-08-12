package app.yapam.secret.repository;

import org.springframework.data.r2dbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;

@Repository
public interface SecretRepository extends ReactiveCrudRepository<SecretDao, String> {

    @Transactional
    void deleteBySecretId(String secretId);

    Mono<SecretDao> findFirstBySecretIdAndVersion(String secretId, Integer version);

    Mono<SecretDao> findFirstBySecretIdOrderByVersionDesc(String secretId);

    Mono<SecretVersionProjection> findFirstDistinctVersionBySecretIdOrderByVersionDesc(String secretId);

    @Query(value = "select s1 from SecretDao s1 left outer join SecretDao s2 on s1.secretId = s2.secretId and s1.version < s2.version where s2.version is null and s1.user.id=:userId")
    Flux<SecretDao> highestSecretsForUser(@Param("userId") String userId);
}
