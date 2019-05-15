package me.raddatz.jwarden.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDBO, String> {

    UserDBO findOneById(String userId);
    UserDBO findOneByEmail(String name);
}
