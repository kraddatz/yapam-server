package me.raddatz.yapam.secret.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SecretRepository extends JpaRepository<SecretDBO, String> {

    @Query(value = "select s.version from secret s where s.secret_id = :secret_id order by s.version desc limit 1", nativeQuery = true)
    Integer findFirstVersionBySecretIdOrderByVersionDesc(@Param("secret_id") String secretId);
}
