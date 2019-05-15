package me.raddatz.yapam.secret.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SecretRepository extends JpaRepository<SecretDBO, String> {

    Integer findFirstVersionBySecretIdOrderByVersionDesc(@Param("secretId") String secretId);
}
