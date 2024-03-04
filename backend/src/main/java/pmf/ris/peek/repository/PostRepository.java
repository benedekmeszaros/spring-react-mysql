package pmf.ris.peek.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pmf.ris.peek.model.Gallery;
import pmf.ris.peek.model.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{
	boolean existsByTitle(String title);
	
	@Query("select p from Post p where p.title = :title and p.gallery.id = :id")
	Optional<Post> exexistsByTitleAndGallery(String title, Integer id);
	
	@Query("select p from Post p order by p.createDate desc")
	List<Post> findAllSorted();
	
	
	@Query("select p from Post p where p.gallery = :gallery order by p.createDate desc")
	List<Post> findByGallerySorted(Gallery gallery);
	
	@Query("select p from Post p where DATE_FORMAT(p.createDate, '%Y-%m-%d') = DATE_FORMAT(:date, '%Y-%m-%d')")
	List<Post> findAllCreatedAt(Instant date);
}
