package pmf.ris.peek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import pmf.ris.peek.exceptions.NoSuchElementException;
import pmf.ris.peek.model.Role;
import pmf.ris.peek.model.User;
import pmf.ris.peek.repository.RoleRepository;
import pmf.ris.peek.repository.UserRepository;

public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private EntityManager entityManager;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("No " + username + " user found."));
		return new pmf.ris.peek.security.UserDetails(user);
	}
	
	public User findUser(String username) throws NoSuchElementException {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new NoSuchElementException("No " + username + " user found."));
	}
	
	public User findUser(Integer id) throws NoSuchElementException {
		return userRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("No user found with id: "+ id));
	}
	
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}
	
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}
	
	public void assignUserRole(User user, String roleName) throws NoSuchElementException{
		Role role = roleRepository.findByName(roleName)
				.orElseThrow(() -> new NoSuchElementException("No " + roleName + " role found."));
		user.getRoles().add(role);
	}
	
	public int saveUser(User user) {
		try {
			return userRepository.save(user).getId();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
    @Transactional
    public void unfollowUser(int followerId, int followingId) {
    	Query query = entityManager.createNativeQuery(
                "DELETE FROM user_followers WHERE (follower_id = :followerId AND following_id = :followingId) OR (follower_id = :followingId AND following_id = :followerId)");
        query.setParameter("followerId", followerId);
        query.setParameter("followingId", followingId);
        query.executeUpdate();
    }
	
}
