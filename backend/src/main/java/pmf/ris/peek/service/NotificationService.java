package pmf.ris.peek.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import pmf.ris.peek.model.Notification;
import pmf.ris.peek.model.User;
import pmf.ris.peek.repository.NotificationRepository;

@Service
public class NotificationService {
	@Autowired
	private NotificationRepository notificationRepository;
	
	public Notification findNotificationById(Integer id) throws UsernameNotFoundException{
		return notificationRepository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("No noification found with id: "+ id));
	}
	
	public List<Notification> findAllNotificationsByUser(User user){
		return notificationRepository.findByReceiverSorted(user);
	}
	
	@Transactional
	public void seenAllNotifications(User user) {
		//notificationRepository.seenAll(user.getId());
	}
	
	public int saveNotification(Notification notification) {
		try {
			return notificationRepository.save(notification).getId();
		} catch (Exception e) {
			return -1;
		}
	}
}
