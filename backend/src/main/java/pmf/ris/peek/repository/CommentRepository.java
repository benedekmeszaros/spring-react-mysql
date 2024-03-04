package pmf.ris.peek.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pmf.ris.peek.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer>{
	@Query("select c from Comment c where DATE_FORMAT(c.createDate, '%Y-%m-%d') = DATE_FORMAT(:date, '%Y-%m-%d')")
	List<Comment> findAllCreatedAt(Instant date);
}
