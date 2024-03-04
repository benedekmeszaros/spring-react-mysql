package pmf.ris.peek.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pmf.ris.peek.dto.UserInfoDTO;
import pmf.ris.peek.dto.UserUpdateDTO;
import pmf.ris.peek.exceptions.DuplicateEntryException;
import pmf.ris.peek.exceptions.NoSuchElementException;
import pmf.ris.peek.exceptions.UnsupportedOperationException;
import pmf.ris.peek.model.User;
import pmf.ris.peek.service.ImageService;
import pmf.ris.peek.service.UserService;

@RestController
@CrossOrigin(origins = "${cross.allowed-origin}", allowCredentials = "true")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ImageService imageService;

	@GetMapping("private/user/self")
	public ResponseEntity<?> getSelf() throws NoSuchElementException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			User user = userService.findUser(authentication.getName());
			UserInfoDTO userInfo = UserInfoDTO.builder().id(user.getId()).email(user.getEmail())
					.username(user.getUsername()).description(user.getDescription())
					.registrationDate(user.getRegistrationDate()).image(user.getImage())
					.numberOfGalleries(user.getGalleries().size())
					.followers(user.getFollowers().stream().map(f -> f.getUsername()).toList())
					.followings(user.getFollowing().stream().map(f -> f.getUsername()).toList())
					.build();
			return ResponseEntity.ok(userInfo);
		} else {
			return new ResponseEntity<String>("Unauthorized.", HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping("public/user/{id}")
	public ResponseEntity<?> getUser(@PathVariable("id") Integer id) throws NoSuchElementException {
		User user = userService.findUser(id);
		UserInfoDTO userInfo = UserInfoDTO.builder().id(user.getId()).email("").username(user.getUsername())
				.description(user.getDescription()).registrationDate(user.getRegistrationDate()).image(user.getImage())
				.numberOfGalleries(user.getGalleries().size())
				.followers(user.getFollowers().stream().map(f -> f.getUsername()).toList())
				.followings(user.getFollowing().stream().map(f -> f.getUsername()).toList())
				.build();
		return ResponseEntity.ok(userInfo);
	}

	@PostMapping("private/user/update")
	public ResponseEntity<?> updateSelf(@ModelAttribute UserUpdateDTO update) throws NoSuchElementException, UnsupportedOperationException, DuplicateEntryException  {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			User user = userService.findUser(authentication.getName());
			if (update.getDescription() != null)
				user.setDescription(update.getDescription());
			if (update.getEmail() != null && !update.getEmail().equals(""))
			{
				if(userService.existsByEmail(update.getEmail()))
					throw new DuplicateEntryException("Given e-mail address is already used.");
				user.setEmail(update.getEmail());
			}
			if (update.getPassword() != null && !update.getPassword().equals(""))
				user.setPassword(passwordEncoder.encode(update.getPassword()));
			if (update.getImage() != null) {
				if (user.getImage() != null)
					imageService.deleteFromFileSystem(user.getImage());
				user.setImage(imageService.saveToFileSystem(update.getImage(), "avatars",
						user.getId() + "-" + UUID.randomUUID().toString()));
			}
			int id = userService.saveUser(user);
			if(id == -1)
				throw new UnsupportedOperationException("Update rejected.");
			else
			{
				UserInfoDTO userInfo = UserInfoDTO.builder().id(user.getId()).email(user.getEmail())
						.username(user.getUsername()).description(user.getDescription())
						.registrationDate(user.getRegistrationDate()).image(user.getImage())
						.numberOfGalleries(user.getGalleries().size())
						.followers(user.getFollowers().stream().map(f -> f.getUsername()).toList())
						.build();
				return ResponseEntity.ok(userInfo);
			}
		} else {
			return new ResponseEntity<String>("Unauthorized.", HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping("private/user/follow")
	public ResponseEntity<?> followUser(@RequestParam Integer id) throws UnsupportedOperationException, NoSuchElementException   {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			User follower = userService.findUser(authentication.getName());
			User following = userService.findUser(id);
			if (follower.getId() == following.getId()) {
				throw new UnsupportedOperationException("You can not follow yourself.");
			}
			
			follower.getFollowing().add(following);
			following.getFollowers().add(follower);
			
			int id1 = userService.saveUser(follower);
			int id2 = userService.saveUser(following);
			if(id1 == -1 || id2 == -1)
				throw new UnsupportedOperationException("Faild to follow " + following.getUsername() + ".");
			else
				return ResponseEntity.ok("You now follow \"" + following.getUsername() + "\".");
		} else {
			return new ResponseEntity<String>("Unauthorized.", HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PostMapping("private/user/unfollow")
	public ResponseEntity<?> unfollowUser(@RequestParam Integer id) throws UnsupportedOperationException, NoSuchElementException   {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			User follower = userService.findUser(authentication.getName());
			User following = userService.findUser(id);

			if (follower.getId() == following.getId()) {
				throw new UnsupportedOperationException("You can not unfollow yourself.");
			}	
			userService.unfollowUser(follower.getId(), following.getId());
			return ResponseEntity.ok(follower.getUsername() + " now unfollow \"" + following.getUsername() + "\".");
		} else {
			return new ResponseEntity<String>("Unauthorized.", HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping("private/user/following/all")
	public ResponseEntity<?> getFollowings() throws NoSuchElementException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			User user = userService.findUser(authentication.getName());
			return ResponseEntity.ok(user.getFollowing().stream()
					.map(f -> UserInfoDTO.builder().id(f.getId()).email("").username(f.getUsername())
							.description(f.getDescription()).registrationDate(f.getRegistrationDate())
							.image(f.getImage())
							.followers(f.getFollowers().stream().map(fo -> fo.getUsername()).toList())
							.build())		
					.toList());
		} else {
			return new ResponseEntity<String>("Unauthorized.", HttpStatus.UNAUTHORIZED);
		}
	}
}
