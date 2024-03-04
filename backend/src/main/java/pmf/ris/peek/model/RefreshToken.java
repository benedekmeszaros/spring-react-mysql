package pmf.ris.peek.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "refresh_token")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(unique = true, nullable = false)
	private String value;
	@Column(nullable = false, name = "expiration_date")
	private Instant expirationDate;
	@OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;
}
