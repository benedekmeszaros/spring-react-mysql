package pmf.ris.peek.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pmf.ris.peek.exceptions.NoSuchElementException;
import pmf.ris.peek.model.Gallery;
import pmf.ris.peek.model.Post;
import pmf.ris.peek.repository.PostRepository;

@Service
public class PostService {
	@Autowired
	private PostRepository postRepository;
	
	public Post findPostById(Integer id) throws NoSuchElementException   {
		return postRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("No post found with id: " + id));
	}
	
	public List<Post> findAllByGallerySorted(Gallery gallery)
	{
		return postRepository.findByGallerySorted(gallery);
	}
	
	public List<Post> findAllPostCreatedAt(Instant date){
		return postRepository.findAllCreatedAt(date);
	}
	
	public boolean existsPostByTitle(String title) {
		return postRepository.existsByTitle(title);
	}
	
	public boolean existsPOstByTitleAndGallery(String title, Integer galleryId) {
		return postRepository.exexistsByTitleAndGallery(title, galleryId).isPresent();
	}
	
	public boolean deletePost(Post post) {
		try {
			postRepository.delete(post);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public int savePost(Post post) {
		try {
			return postRepository.save(post).getId();
		} catch (Exception e) {
			return -1;
		}
	}
}
