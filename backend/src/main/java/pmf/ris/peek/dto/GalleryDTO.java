package pmf.ris.peek.dto;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class GalleryDTO {
	private long numberOfComments;
	private long score;
	private int id;
	private String title;
	private String description;
	private Instant createDate;
	private String image;
	private int userId;
	private String username;
	private String userImage;
	private int numberOfPosts;
	private List<String> categories;
	
}
