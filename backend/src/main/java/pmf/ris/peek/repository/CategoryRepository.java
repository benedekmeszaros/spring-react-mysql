package pmf.ris.peek.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pmf.ris.peek.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	Optional<Category> findByName(String name);
	boolean existsByName(String name);
}
