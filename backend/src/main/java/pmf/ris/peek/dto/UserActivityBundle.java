package pmf.ris.peek.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserActivityBundle {
	private int userId;
	private String username;
	private String image;
	private String description;
	private String action;
	private Instant date;
}