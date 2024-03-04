package pmf.ris.peek.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import pmf.ris.peek.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByUsername(String username);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	
	@Modifying
	@Query("update User u set u.password = :password, u.description = :description, u.image = :image, u.email = :email where u.id = :id")
	int update(Integer id, String password, String description, String email, String image);
}
