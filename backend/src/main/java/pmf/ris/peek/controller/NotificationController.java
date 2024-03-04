package pmf.ris.peek.controller;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pmf.ris.peek.dto.NotificationInfoDTO;
import pmf.ris.peek.exceptions.NoSuchElementException;
import pmf.ris.peek.model.Notification;
import pmf.ris.peek.model.User;
import pmf.ris.peek.service.NotificationService;
import pmf.ris.peek.service.UserService;

@RestController
@RequestMapping("private/user/notification")
@CrossOrigin(origins = "${cross.allowed-origin}", allowCredentials = "true")
public class NotificationController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private NotificationService notificationService;	
	
	@GetMapping("all")
	public ResponseEntity<?> getNotifications() throws NoSuchElementException{		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			User user = userService.findUser(authentication.getName());
			return ResponseEntity.ok(notificationService.findAllNotificationsByUser(user)
					.stream()
					.map(n -> NotificationInfoDTO.builder()
							.id(n.getId())
							.content(n.getContent())
							.action(n.getAction())
							.userImage(n.getSender().getImage())
							.sentDate(n.getSentDate())
							.seen(n.isSeen())
							.build())
					.toList());
		} else {
			return new ResponseEntity<String>("Unauthorized.", HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PostMapping("all")
	public ResponseEntity<?> seenNotifications() throws NoSuchElementException{		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			User user = userService.findUser(authentication.getName());
			notificationService.seenAllNotifications(user);
			return ResponseEntity.ok("All notifications set to seen.");
		} else {
			return new ResponseEntity<String>("Unauthorized.", HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PostMapping("seen/{id}")
	public ResponseEntity<?> markAsSeen(@PathVariable Integer id) throws NoSuchElementException, UnsupportedAudioFileException{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			Notification notification =	notificationService.findNotificationById(id);
			notification.setSeen(true);
			if(notificationService.saveNotification(notification) == -1)
				throw new UnsupportedAudioFileException("Failed to update notification status.");
			else
				return ResponseEntity.ok("All notifications set to seen.");
		} else {
			return new ResponseEntity<String>("Unauthorized.", HttpStatus.UNAUTHORIZED);
		}
	}
}
