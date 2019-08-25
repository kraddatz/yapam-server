package app.yapam.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UserSecretRepository extends JpaRepository<UserSecretDao, UserSecretId> {

    Set<UserSecretDao> findAllByUserId(String userId);
}
