package pmf.ris.peek.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostCreateDTO {
	@NotBlank
	private String title;
	@NotBlank
	private String description;
	@NotNull
	private MultipartFile image;
}
