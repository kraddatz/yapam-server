package me.raddatz.jwarden.user.repository;

import me.raddatz.jwarden.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findOneById(String userId);
    User findOneByEmail(String name);
}
