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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "gallery")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Gallery {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false, unique = true)
	private String title;
	@Column
	private String description;
	@Column(name = "create_date", nullable = false)
	private Instant createDate;
	@Column(name = "img", nullable = false)
	private String image;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@ManyToMany(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
	@JoinTable( name = "gallery_category", 
			  joinColumns = @JoinColumn(name = "gallery_id", referencedColumnName = "id"), 
			  inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
	private List<Category> categories;
	
	@OneToMany(mappedBy="gallery", cascade = CascadeType.REMOVE)
	private List<Post> posts;
}
