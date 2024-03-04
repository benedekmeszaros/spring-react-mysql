package pmf.ris.peek.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pmf.ris.peek.model.Vote;

public interface VoteRepository extends JpaRepository<Vote, Integer> {
	@Query("select v from Vote v where v.user.id = :userId and v.post.id = :postId")
	List<Vote> findByUserAndPost(Integer userId, Integer postId);
}
