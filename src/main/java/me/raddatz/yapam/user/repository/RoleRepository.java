package me.raddatz.yapam.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleDBO, String> {

	List<RoleDBO> findByUser(UserDBO user);

}