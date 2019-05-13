package me.raddatz.jwarden.user.repository;

import me.raddatz.jwarden.user.model.Role;
import me.raddatz.jwarden.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

	List<Role> findByUser(User user);

}