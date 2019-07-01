package app.yapam.secret.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
public interface SecretRepository extends JpaRepository<SecretDBO, String> {

    SecretVersionProjection findFirstDistinctVersionBySecretIdOrderByVersionDesc(String secretId);

    SecretDBO findFirstBySecretIdOrderByVersionDesc(String secretId);

    @Query(value = "select s1 from SecretDBO s1 left outer join SecretDBO s2 on s1.secretId = s2.secretId and s1.version < s2.version where s2.version is null and s1.user.id=:userId")
    Set<SecretDBO> highestSecretsForUser(@Param("userId") String userId);

    SecretDBO findFirstBySecretIdAndVersion(String secretId, Integer version);

    @Transactional
    void deleteBySecretId(String secretId);
}
