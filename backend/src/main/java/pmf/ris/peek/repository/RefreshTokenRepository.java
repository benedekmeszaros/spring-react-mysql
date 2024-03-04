package pmf.ris.peek.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pmf.ris.peek.model.RefreshToken;
import pmf.ris.peek.model.User;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
	boolean existsByValue(String value);
	boolean existsByOwner(User owner);
	Optional<RefreshToken> findByValue(String value);
	Optional<RefreshToken> findByOwner(User owner);
}
