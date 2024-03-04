package pmf.ris.peek.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public abstract class AuthDTO {
	public record Request(String username, String password) {}
	public record Response(String accessToken, List<String> roles) {}
	public record Refresh (String refreshToken) {}
	public record Register(@Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$") String email, 
			@NotBlank String username, 
			@NotBlank String password) {}
}
