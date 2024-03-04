package pmf.ris.peek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pmf.ris.peek.model.Notification;
import java.util.List;
import pmf.ris.peek.model.User;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
	List<Notification> findByReceiver(User receiver);
	
	@Query("select n from Notification n where n.receiver = :user order by n.sentDate desc")
	List<Notification> findByReceiverSorted(User user);
}
