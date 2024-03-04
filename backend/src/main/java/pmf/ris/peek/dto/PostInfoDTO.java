package pmf.ris.peek.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PostInfoDTO {
	private int id;
	private String title;
	private String description;
	private Instant createDate;
	private String username;
	private String image;
	private int numberOfComments;
	private int score;
}
