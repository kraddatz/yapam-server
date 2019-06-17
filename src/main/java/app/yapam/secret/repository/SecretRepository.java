package app.yapam.secret.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
public interface SecretRepository extends JpaRepository<SecretDBO, String> {

    SecretDBO findFirstBySecretIdOrderByVersionDesc(String secretId);

    @Query(value = "select s1.* from secret s1 left outer join secret s2 on s1.secret_id = s2.secret_id and s1.version < s2.version where s2.version is null and s1.user_id=:userId", nativeQuery = true)
    Set<SecretDBO> findAllByCurrentByUser(@Param("userId") String userId);

    @Query(value = "select s.version from secret s where s.secret_id = :secretId order by s.version desc limit 1", nativeQuery = true)
    Integer findCurrentVersion(@Param("secretId") String secretId);

    @Transactional
    void deleteBySecretId(String secretId);
}
