package pmf.ris.peek.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "vote")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vote {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable =  false)
	private int status;
	
	@ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
	
	@ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
