package app.yapam.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDao, String> {

    UserDao findOneByEmail(String email);

    UserDao findOneById(String userId);
}
