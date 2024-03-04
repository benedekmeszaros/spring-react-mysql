package pmf.ris.peek.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pmf.ris.peek.model.Comment;
import pmf.ris.peek.repository.CommentRepository;

@Service
public class CommentService {
	@Autowired
	private CommentRepository commentRepository;
	
	public Comment findCommentBy(Integer id) throws UsernameNotFoundException{
		return commentRepository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("No comment found with id: " + id));
	}
	
	public List<Comment> findAllCommentCreatedAt(Instant date){
		return commentRepository.findAllCreatedAt(date);
	}
	
	public int saveComment(Comment comment) {
		try {
			return commentRepository.save(comment).getId();
		} catch (Exception e) {
			return -1;
		}
	}
}
