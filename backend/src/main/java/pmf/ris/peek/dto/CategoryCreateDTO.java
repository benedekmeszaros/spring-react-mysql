package pmf.ris.peek.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryCreateDTO {
	@NotBlank
	private String category;
}
