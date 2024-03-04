package pmf.ris.peek.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CommentInfoDTO {
	private String username;
	private String userImage;
	private Instant createDate;
	private String content;
}
