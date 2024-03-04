package pmf.ris.peek.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pmf.ris.peek.model.Gallery;
import pmf.ris.peek.model.User;

public interface GalleryRepository extends JpaRepository<Gallery, Integer> {
	
	@Query("SELECT SUM(v.status) FROM Gallery g JOIN g.posts p JOIN p.votes v WHERE g.id = :id")
	Long getScore(Integer id);
	
	@Query("SELECT COUNT(c.id) FROM Gallery g JOIN g.posts p JOIN p.comments c WHERE g.id = :id")
	Long getNumberOfComments(Integer id);
	
	boolean existsByTitle(String title);
	
	List<Gallery> findByUser(User user);
	
	@Query("select g from Gallery g order by g.createDate desc")
	List<Gallery> findAllSorted();
	
	@Query("select g from Gallery g where g.user = :user order by g.createDate desc")
	List<Gallery> findByUserSorted(User user);
	
	@Query("select g from Gallery g where DATE_FORMAT(g.createDate, '%Y-%m-%d') = DATE_FORMAT(:date, '%Y-%m-%d')")
	List<Gallery> findAllCreatedAt(Instant date);
}
