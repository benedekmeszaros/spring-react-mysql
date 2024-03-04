package pmf.ris.peek.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentCreateDTO {
	@NotBlank
	private String content;
}
