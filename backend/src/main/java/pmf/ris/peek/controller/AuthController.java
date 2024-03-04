package pmf.ris.peek.controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import pmf.ris.peek.dto.AuthDTO;
import pmf.ris.peek.exceptions.DuplicateEntryException;
import pmf.ris.peek.exceptions.NoSuchElementException;
import pmf.ris.peek.exceptions.TokenExpiredException;
import pmf.ris.peek.exceptions.UnsupportedOperationException;
import pmf.ris.peek.model.Role;
import pmf.ris.peek.model.User;
import pmf.ris.peek.security.TokenProvider;
import pmf.ris.peek.service.UserService;

@RestController
@RequestMapping("auth")
@CrossOrigin(origins = "${cross.allowed-origin}", allowCredentials = "true")
public class AuthController {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private TokenProvider tokenProvider;
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("signin")
	public ResponseEntity<AuthDTO.Response> signIn(HttpServletResponse response, @RequestBody AuthDTO.Request request) throws NoSuchElementException {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
		String accessToken = tokenProvider.generateAccessToken(authentication.getName(), roles);
		String refreshToken = tokenProvider.generateRefreshToken(authentication.getName());
		ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken).secure(true).httpOnly(true)
				.path("/peek/auth").maxAge(60 * 60 * 24).sameSite("None").build();
		response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
		return ResponseEntity.ok(new AuthDTO.Response(accessToken, roles));
	}

	@PostMapping("signup")
	public ResponseEntity<String> signUp(@Valid @RequestBody AuthDTO.Register register)
			throws DuplicateEntryException, NoSuchElementException, UnsupportedOperationException {
		if (userService.existsByUsername(register.username()))
			throw new DuplicateEntryException(
					String.format("Username \"%s\" is already reserved.", register.username()));
		else if (userService.existsByEmail(register.email()))
			throw new DuplicateEntryException(
					String.format("Email address \"%s\" is already reserved.", register.email()));
		else {
			User user = User.builder().email(register.email()).username(register.username())
					.password(passwordEncoder.encode(register.password())).registrationDate(Instant.now())
					.description("").image("/images/avatars/default.png").roles(new ArrayList<Role>()).build();
			userService.assignUserRole(user, "USER");
			int id = userService.saveUser(user);
			if (id == -1)
				throw new UnsupportedOperationException("Registration failed.");
			else
				return ResponseEntity.ok("Register success.");
		}
	}

	@GetMapping("refresh")
	public ResponseEntity<?> refresh(HttpServletRequest request) throws NoSuchElementException, TokenExpiredException {
		String refreshToken = null;
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("refreshToken")) {
					refreshToken = cookie.getValue();
					break;
				}
			}
		}
		if (refreshToken == null)
			return new ResponseEntity<String>("Missing refresh token.", HttpStatus.UNAUTHORIZED);

		User user = tokenProvider.validateRefreshToken(refreshToken);
		List<String> roles = user.getRoles().stream().map(r -> r.getName()).toList();
		String accessToken = tokenProvider.generateAccessToken(user.getUsername(), roles);
		return ResponseEntity.ok(new AuthDTO.Response(accessToken, roles));
	}

	@PostMapping("signout")
	public ResponseEntity<String> signOut(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedOperationException, NoSuchElementException {
		String refreshToken = null;
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("refreshToken")) {
					refreshToken = cookie.getValue();
					break;
				}
			}
		}
		if (refreshToken == null)
			throw new UnsupportedOperationException("No user signed-in.");
		else {
			tokenProvider.deleteRefreshToken(refreshToken);
			ResponseCookie responseCookie = ResponseCookie.from("refreshToken", "").secure(true).httpOnly(true)
					.path("/peek/auth").maxAge(0).sameSite("None").build();
			response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
			return ResponseEntity.ok("You sign-out.");
		}
	}
}
