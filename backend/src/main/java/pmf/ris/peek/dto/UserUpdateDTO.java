package pmf.ris.peek.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UserUpdateDTO {
	private String email;
	private String description;
	private String password;
	private MultipartFile image;
}
