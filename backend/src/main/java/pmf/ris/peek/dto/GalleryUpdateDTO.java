package pmf.ris.peek.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class GalleryUpdateDTO {
	private String title;
	private String description;
	private MultipartFile image;
	private List<String> categories;
}
