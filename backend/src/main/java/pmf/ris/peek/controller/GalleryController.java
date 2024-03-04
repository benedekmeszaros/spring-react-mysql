package pmf.ris.peek.controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import pmf.ris.peek.dto.CommentCreateDTO;
import pmf.ris.peek.dto.CommentInfoDTO;
import pmf.ris.peek.dto.GalleryCreateDTO;
import pmf.ris.peek.dto.GalleryDTO;
import pmf.ris.peek.dto.GalleryUpdateDTO;
import pmf.ris.peek.dto.PostCreateDTO;
import pmf.ris.peek.dto.PostInfoDTO;
import pmf.ris.peek.dto.PostUpdateDTO;
import pmf.ris.peek.dto.UserActivityBundle;
import pmf.ris.peek.exceptions.DuplicateEntryException;
import pmf.ris.peek.exceptions.NoSuchElementException;
import pmf.ris.peek.exceptions.UnsupportedOperationException;
import pmf.ris.peek.model.Category;
import pmf.ris.peek.model.Comment;
import pmf.ris.peek.model.Gallery;
import pmf.ris.peek.model.Notification;
import pmf.ris.peek.model.Post;
import pmf.ris.peek.model.User;
import pmf.ris.peek.model.Vote;
import pmf.ris.peek.service.CategoryService;
import pmf.ris.peek.service.CommentService;
import pmf.ris.peek.service.GalleryService;
import pmf.ris.peek.service.ImageService;
import pmf.ris.peek.service.NotificationService;
import pmf.ris.peek.service.PostService;
import pmf.ris.peek.service.UserService;
import pmf.ris.peek.service.VoteService;

@RestController
@CrossOrigin(origins = "${cross.allowed-origin}", allowCredentials = "true")
public class GalleryController {
	@Autowired
	private GalleryService galleryService;
	@Autowired
	private UserService userService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ImageService imageService;
	@Autowired
	private PostService postService;
	@Autowired
	private NotificationService notificationService;	
	@Autowired
	private CommentService commentService;
	@Autowired
	private VoteService voteService;
	
	@GetMapping("public/gallery/all")
	public ResponseEntity<?> getGalleries(){
		return ResponseEntity.ok(galleryService.findAllSorted()
				.stream()
				.map(g -> GalleryDTO.builder()
						.id(g.getId())
						.score(galleryService.getScoreOfGallery(g))
						.numberOfComments(galleryService.getNumberOfComments(g))
						.title(g.getTitle())
						.description(g.getDescription())
						.createDate(g.getCreateDate())
						.image(g.getImage())
						.userId(g.getUser().getId())
						.numberOfPosts(g.getPosts().size())
						.username(g.getUser().getUsername())
						.userImage(g.getUser().getImage())
						.categories(g.getCategories().stream()
								.map(c -> c.getName())
								.toList())
						.build())
				.toList()
				);
		
	}
	
	@GetMapping("public/gallery/user/{id}/all")
	public ResponseEntity<?> getGalleriesByUser(@PathVariable Integer id) throws NoSuchElementException{
		User user = userService.findUser(id);
		return ResponseEntity.ok(galleryService.findAllByUserSorted(user)
				.stream()
				.map(g -> GalleryDTO.builder()
						.id(g.getId())
						.score(galleryService.getScoreOfGallery(g))
						.numberOfComments(galleryService.getNumberOfComments(g))
						.title(g.getTitle())
						.description(g.getDescription())
						.createDate(g.getCreateDate())
						.image(g.getImage())
						.userId(g.getUser().getId())
						.numberOfPosts(g.getPosts().size())
						.username(g.getUser().getUsername())
						.userImage(g.getUser().getImage())
						.categories(g.getCategories().stream()
								.map(c -> c.getName())
								.toList())
						.build())
				.toList()
				);
	}
	
	@GetMapping("private/user/gallery/self")
	public ResponseEntity<?> getSelfGalleries() throws NoSuchElementException{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			User user = userService.findUser(authentication.getName());
			return ResponseEntity.ok(galleryService.findAllByUserSorted(user)
					.stream()
					.map(g -> GalleryDTO.builder()
							.id(g.getId())
							.score(galleryService.getScoreOfGallery(g))
							.numberOfComments(galleryService.getNumberOfComments(g))
							.title(g.getTitle())
							.description(g.getDescription())
							.createDate(g.getCreateDate())
							.image(g.getImage())
							.userId(g.getUser().getId())
							.username(g.getUser().getUsername())
							.userImage(g.getUser().getImage())
							.numberOfPosts(g.getPosts().size())
							.categories(g.getCategories().stream()
									.map(c -> c.getName())
									.toList())
							.build())
					.toList()
					);
		} else {
			return new ResponseEntity<String>("Unauthorized.", HttpStatus.UNAUTHORIZED);
		}
	}
	
	@GetMapping("public/gallery/{id}")
	public ResponseEntity<?> getGallery(@PathVariable("id") Integer id) throws NoSuchElementException{
		Gallery g = galleryService.findGalleryById(id);
		return ResponseEntity.ok(GalleryDTO.builder()
				.id(g.getId())
				.score(galleryService.getScoreOfGallery(g))
				.numberOfComments(galleryService.getNumberOfComments(g))
				.title(g.getTitle())
				.description(g.getDescription())
				.createDate(g.getCreateDate())
				.image(g.getImage())
				.userId(g.getUser().getId())
				.username(g.getUser().getUsername())
				.userImage(g.getUser().getImage())
				.numberOfPosts(g.getPosts().size())
				.categories(g.getCategories().stream()
						.map(c -> c.getName())
						.toList())
				.build());
	}
	
	@PostMapping("private/user/put/gallery")
	public ResponseEntity<?> createGallery(@ModelAttribute @Valid GalleryCreateDTO create) throws NoSuchElementException, DuplicateEntryException{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			User user = userService.findUser(authentication.getName());
			
			if(galleryService.existsGalleryByTitle(create.getTitle()))
				throw new DuplicateEntryException("Title "+ create.getTitle()+" already exists.");	
			List<Category> categories = new ArrayList<Category>();
			for(String c: create.getCategories())
					categories.add(categoryService.findCategory(c));
			Gallery gallery = Gallery.builder()
					.title(create.getTitle())
					.description(create.getDescription())
					.createDate(Instant.now())
					.user(user)
					.categories(categories)
					.image(imageService.saveToFileSystem(create.getImage(), "galleries", user.getId() + create.getTitle()))
					.build();
			Integer id = galleryService.saveGallery(gallery);
			for(User follower : user.getFollowers()) {
				Notification notification = Notification.builder()
						.content(String.format("New gallery by %s.", user.getUsername()))
						.sentDate(Instant.now())
						.action(String.format("/gallery/%d", id))
						.sender(user)
						.receiver(follower)
						.seen(false)
						.build();
				notificationService.saveNotification(notification);
			}
			if(id == -1)
				return new ResponseEntity<String>("Faild to save.", HttpStatus.BAD_REQUEST);
			else
				return ResponseEntity.ok(id);
		} else {
			return new ResponseEntity<String>("Unauthorized.", HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PostMapping("private/user/delete/gallery/{id}")
	public ResponseEntity<?> deleteGallery(@PathVariable Integer id) throws NoSuchElementException, UnsupportedOperationException{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			User user = userService.findUser(authentication.getName());
			Gallery gallery = galleryService.findGalleryById(id);
			gallery.getCategories().clear();	
			if(!user.getGalleries().contains(gallery))
				throw new UnsupportedOperationException("You can not delete other's gallery.");
			
			for(Post post : gallery.getPosts())
			{
				if(postService.deletePost(post))
					imageService.deleteFromFileSystem(post.getImage());
			}
			gallery.getPosts().clear();
			imageService.deleteFromFileSystem(gallery.getImage());
			
			if(!galleryService.deleteGallery(gallery))	
				throw new UnsupportedOperationException("Failed to delete gallery.");
			else
				return ResponseEntity.ok(id);
		} else {
			return new ResponseEntity<String>("Unauthorized.", HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PostMapping("private/user/update/gallery/{id}")
	public ResponseEntity<?> updateGallery(@PathVariable Integer id, @ModelAttribute GalleryUpdateDTO update) throws NoSuchElementException, UnsupportedOperationException{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			User user = userService.findUser(authentication.getName());
			Gallery gallery = galleryService.findGalleryById(id);

			if(user.getId() != gallery.getUser().getId())
				throw new UnsupportedOperationException("You can not edit other's gallery.");
			
			if(update.getCategories() == null || update.getCategories().isEmpty())
				throw new UnsupportedOperationException("Each gallery must have least one category.");
			
			if(!update.getTitle().equals(gallery.getTitle()) && galleryService.existsGalleryByTitle(update.getTitle()))
				throw new UnsupportedOperationException("Title is already in use");
				
			List<Category> categories = new ArrayList<Category>();
			for(String c: update.getCategories())
					categories.add(categoryService.findCategory(c));
			
			gallery.setCategories(categories);
			gallery.setTitle(update.getTitle());
			if(update.getDescription() != null)
				gallery.setDescription(update.getDescription());
			
			if(update.getImage() != null)
			{
				imageService.deleteFromFileSystem(gallery.getImage());
				gallery.setImage(imageService.saveToFileSystem(update.getImage(), "galleries", user.getId() + update.getTitle()));
			}
			
			if(galleryService.saveGallery(gallery) == -1)
				throw new UnsupportedOperationException("Failed to update gallery");
			else
				return ResponseEntity.ok("Gallery updated.");
		} else {
			return new ResponseEntity<String>("Unauthorized.", HttpStatus.UNAUTHORIZED);
		}
	}
	
	@GetMapping("public/gallery/{id}/post/all")
	public ResponseEntity<?> getGalleryPosts(@PathVariable Integer id) throws NoSuchElementException{
		Gallery gallery = galleryService.findGalleryById(id);
		return ResponseEntity.ok(postService.findAllByGallerySorted(gallery)
				.stream()
				.map(p -> PostInfoDTO.builder()
						.id(p.getId())
						.title(p.getTitle())
						.description(p.getDescription())
						.image(p.getImage())
						.numberOfComments(p.getComments().size())
						.score(p.getVotes().stream().map(v -> v.getStatus()).mapToInt(Integer::intValue).sum())
						.createDate(p.getCreateDate())
						.username(gallery.getUser().getUsername())
						.build()));
	}
	
	@PostMapping("private/user/gallery/{id}/put/post")
	public ResponseEntity<?> createPost(@PathVariable Integer id, @ModelAttribute @Valid PostCreateDTO create) throws NoSuchElementException, DuplicateEntryException, UnsupportedOperationException{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			Gallery gallery = galleryService.findGalleryById(id);
			if(postService.existsPOstByTitleAndGallery( create.getTitle(),gallery.getId()))
				throw new DuplicateEntryException("Title "+ create.getTitle()+" already exists.");
			Post post = Post.builder()
					.title(create.getTitle())
					.description(create.getDescription())
					.gallery(gallery)
					.createDate(Instant.now())
					.image(imageService.saveToFileSystem(create.getImage(), "posts", gallery.getId() + create.getTitle()))
					.build();
			int postId = postService.savePost(post);
			if(postId == -1)
				throw new UnsupportedOperationException("Faild to create new post.");
			else {
				PostInfoDTO postInfo = PostInfoDTO.builder()
						.id(postId)
						.username(gallery.getUser().getUsername())
						.title(post.getTitle())
						.description(post.getDescription())
						.image(post.getImage())
						.numberOfComments(0)
						.score(0)
						.createDate(post.getCreateDate())
						.build();
				for(User follower : gallery.getUser().getFollowers()) {
					Notification notification = Notification.builder()
							.content(String.format("New post by %s.", gallery.getUser().getUsername()))
							.sentDate(Instant.now())
							.action(String.format("/gallery/%d~%d", id, postId))
							.sender(gallery.getUser())
							.receiver(follower)
							.seen(false)
							.build();
					notificationService.saveNotification(notification);
				}
				return ResponseEntity.ok(postInfo);
			}
		} else {
			return new ResponseEntity<String>("Unauthorized.", HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PostMapping("private/user/gallery/{id}/delete/post")
	public ResponseEntity<?> deletePost(@PathVariable Integer id) throws NoSuchElementException, UnsupportedOperationException{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			 User user = userService.findUser(authentication.getName());
			 Post post = postService.findPostById(id);
			 
			 if(post.getGallery().getUser().getId() != user.getId())
				 throw new UnsupportedOperationException("You can not delete other user's post.");
			 imageService.deleteFromFileSystem(post.getImage());	 
			 if(!postService.deletePost(post))
				 throw new UnsupportedOperationException("Failed to delete the post.");
			 else
				 return ResponseEntity.ok("Post deleted with id: " + post.getId());
		} else {
			return new ResponseEntity<String>("Unauthorized.", HttpStatus.UNAUTHORIZED);
		}
	}
	
	@GetMapping("public/post/{id}/comment/all")
	public ResponseEntity<?> getPostComments(@PathVariable Integer id) throws NoSuchElementException{
		Post post = postService.findPostById(id);
		return ResponseEntity.ok(post.getComments()
				.stream()
				.map(c -> CommentInfoDTO.builder()
						.username(c.getUser().getUsername())
						.userImage(c.getUser().getImage())
						.createDate(c.getCreateDate())
						.content(c.getContent())
						.build())
				.toList());
	}
	
	@PostMapping("private/user/post/{id}/put/comment")
	public ResponseEntity<?> createComment(@PathVariable Integer id ,@RequestBody @Valid CommentCreateDTO create) throws NoSuchElementException, UnsupportedOperationException{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && create != null && !create.getContent().equals("")){
			Post post = postService.findPostById(id);
			User user = userService.findUser(authentication.getName());
			Comment comment = Comment.builder()
					.createDate(Instant.now())
					.content(create.getContent())
					.user(user)
					.post(post)
					.build();

			if(user.getId() != post.getGallery().getUser().getId())
			{
				Notification notification = Notification.builder()
						.content(String.format("New comment by %s.", user.getUsername()))
						.sentDate(Instant.now())
						.action(String.format("/gallery/%d~%d", post.getGallery().getId(),post.getId()))
						.sender(user)
						.receiver(post.getGallery().getUser())
						.seen(false)
						.build();
				notificationService.saveNotification(notification);
			}
			if(commentService.saveComment(comment) == -1 )			
				throw new UnsupportedOperationException("Faild to put new comment.");
				CommentInfoDTO commentInfo = CommentInfoDTO.builder()
					.username(comment.getUser().getUsername())
					.userImage(comment.getUser().getImage())
					.createDate(comment.getCreateDate())
					.content(comment.getContent())
					.build();
			return ResponseEntity.ok(commentInfo);
		} else {
			return new ResponseEntity<String>("Unauthorized.", HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PostMapping("private/user/post/{id}/update")
	public ResponseEntity<?> updatePost(@PathVariable Integer id, @RequestBody @Valid PostUpdateDTO update) throws NoSuchElementException, UnsupportedOperationException, DuplicateEntryException{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			User user = userService.findUser(authentication.getName());
			Post post = postService.findPostById(id);
			
			if(user.getId() != post.getGallery().getUser().getId())
				throw new UnsupportedOperationException("You can not change other's posts.");
			if(!update.getTitle().equals(post.getTitle()) && postService.existsPOstByTitleAndGallery(update.getTitle(), post.getGallery().getId()))
				throw new DuplicateEntryException("Post title is already in use in this gallery.");
			
			post.setTitle(update.getTitle());
			post.setDescription(update.getDescription());
			if(postService.savePost(post) == -1)
				throw new UnsupportedOperationException("Failed to update post.");
			else
				return ResponseEntity.ok("Post updated.");
			} else {
			return new ResponseEntity<String>("Unauthorized.", HttpStatus.UNAUTHORIZED);
		}
	}
	
	@GetMapping("private/user/post/{id}/vote")
	public ResponseEntity<?> getPostStatus(@PathVariable Integer id) throws NoSuchElementException{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			User user = userService.findUser(authentication.getName());
			Post post = postService.findPostById(id);
			Vote vote = voteService.findVoteByPostAndUser(user, post);
			if(vote == null)
				return ResponseEntity.ok(0);
			else
				return ResponseEntity.ok(vote.getStatus());
			} else {
			return new ResponseEntity<String>("Unauthorized.", HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PostMapping("private/user/post/{id}/update/vote")
	public ResponseEntity<?> votePost(@PathVariable Integer id, @RequestParam Integer status) throws NoSuchElementException, UnsupportedOperationException{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && status != null){
			Post post = postService.findPostById(id);
			User user = userService.findUser(authentication.getName());
			
			if(user.getId() == post.getGallery().getUser().getId())
				throw new UnsupportedOperationException("You cannot vote on your posts.");
			if(status > 1 || status < -1)
				throw new UnsupportedOperationException("Invalid status value.");
			Vote vote = voteService.findVoteByPostAndUser(user, post);
			if(vote == null)
			{
				vote = Vote.builder()
						.user(user)
						.post(post)			
						.build();	
			}
			vote.setStatus(status);
			if(voteService.saveVote(vote) == -1)
				throw new UnsupportedOperationException("Faild to vote.");
			else
			return ResponseEntity.ok("Comment added successfully.");
		} else {
			return new ResponseEntity<String>("Unauthorized.", HttpStatus.UNAUTHORIZED);
		}
	}
	
	@GetMapping("private/admin/activity/all")
	public ResponseEntity<?> getUserActivities(@RequestParam String date){
		Instant instant = null;
		try {
			instant = Instant.parse(date);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Invalide date format.");
		}
		
		List<UserActivityBundle> bundles = new ArrayList<>();
		List<Gallery> galleries = galleryService.findAllGalleryCreatedAt(instant);
		for(Gallery gallery : galleries)
		{
			UserActivityBundle bundle = UserActivityBundle.builder()
					.userId(gallery.getUser().getId())
					.username(gallery.getUser().getUsername())
					.image(gallery.getUser().getImage())
					.description("New gallery created.")
					.date(gallery.getCreateDate())
					.action("/gallery/"+gallery.getId())
					.build();
			bundles.add(bundle);
		}
		List<Post> posts = postService.findAllPostCreatedAt(instant);
		for(Post post : posts)
		{
			Gallery gallery = post.getGallery();
			UserActivityBundle bundle = UserActivityBundle.builder()
					.userId(gallery.getUser().getId())
					.username(gallery.getUser().getUsername())
					.image(gallery.getUser().getImage())
					.description("New post created.")
					.date(post.getCreateDate())
					.action("/gallery/"+gallery.getId()+"~"+post.getId())
					.build();
			bundles.add(bundle);
		}
		List<Comment> comments = commentService.findAllCommentCreatedAt(instant);
		for(Comment comment : comments)
		{
			Post post = comment.getPost();
			Gallery gallery = post.getGallery();
			UserActivityBundle bundle = UserActivityBundle.builder()
					.userId(gallery.getUser().getId())
					.username(gallery.getUser().getUsername())
					.image(gallery.getUser().getImage())
					.description("New comment added.")
					.date(post.getCreateDate())
					.action("/gallery/"+gallery.getId()+"~"+post.getId())
					.build();
			bundles.add(bundle);
		}
		return ResponseEntity.ok(bundles);
	}
}
