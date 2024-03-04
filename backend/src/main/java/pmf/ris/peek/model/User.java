package pmf.ris.peek.model;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false, unique = true)
	private String username;
	@Column(nullable = false)
	private String password;
	@Column(name = "registration_date", nullable = false)
	private Instant registrationDate;
	@Column
	private String description;
	@Column(name = "img")
	private String image;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private List<Role> roles;

	@OneToMany(mappedBy = "user")
	private List<Gallery> galleries;

	@OneToMany(mappedBy = "user")
	private List<Comment> comments;

	@OneToMany(mappedBy = "user")
	private List<Vote> votes;
	
	@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_followers",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id")
            
    )
    private List<User> following;

    @ManyToMany(mappedBy = "following")
    private List<User> followers;
    
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<Notification> sentNotifications;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<Notification> receivedNotifications;
}