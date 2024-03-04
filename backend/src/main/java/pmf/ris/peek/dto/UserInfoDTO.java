package pmf.ris.peek.dto;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserInfoDTO {
	private int id;
	private String email;
	private String username;
	private Instant registrationDate;
	private String description;
	private String image;
	private int numberOfGalleries;
	private List<String> followings;
	private List<String> followers;
}
