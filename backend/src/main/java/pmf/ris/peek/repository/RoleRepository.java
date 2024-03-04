package pmf.ris.peek.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pmf.ris.peek.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	Optional<Role> findByName(String name);
}
