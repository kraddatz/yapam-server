package me.raddatz.jwarden.secret.repository;

import me.raddatz.jwarden.secret.model.Secret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecretRepository extends JpaRepository<Secret, String> {
}
