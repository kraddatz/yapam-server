package app.yapam.secret.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SecretRepository extends JpaRepository<SecretDBO, String> {

    SecretDBO findFirstBySecretIdOrderByVersionDesc(String secretId);

    @Query(value = "select s1 from secret s1 left join secret s2 on s1.secretId=s2.secretId and s1.version < s2.version where s2.version is null")
    Set<SecretDBO> findAllByCurrentByUser(String userId);

    @Query(value = "select s.version from secret s where s.secret_id = :secret_id order by s.version desc limit 1", nativeQuery = true)
    Integer findFirstVersionBySecretIdOrderByVersionDesc(@Param("secret_id") String secretId);
}
