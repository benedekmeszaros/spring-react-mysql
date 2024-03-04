package pmf.ris.peek.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GalleryCreateDTO {
	@NotBlank
	private String title;
	@NotBlank
	private String description;
	@NotNull
	private MultipartFile image;
	@NotEmpty
	private List<String> categories;
}
