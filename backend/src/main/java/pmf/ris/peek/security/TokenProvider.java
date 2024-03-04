package pmf.ris.peek.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import pmf.ris.peek.exceptions.NoSuchElementException;
import pmf.ris.peek.exceptions.TokenExpiredException;
import pmf.ris.peek.model.RefreshToken;
import pmf.ris.peek.model.User;
import pmf.ris.peek.repository.RefreshTokenRepository;
import pmf.ris.peek.service.UserService;

@Service
public class TokenProvider {
	@Autowired
	private JwtEncoder jwtEncoder;
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	@Autowired
	private UserService userService;

	public String generateAccessToken(String username, List<String> roles) {
		Instant now = Instant.now();
		JwtClaimsSet claims = JwtClaimsSet.builder().issuer("peek-application").issuedAt(now)
				.expiresAt(now.plus(10, ChronoUnit.MINUTES)).subject(username).claim("roles", roles).build();
		return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}

	public String generateAccessToken(User user) {
		return generateAccessToken(user.getUsername(), user.getRoles().stream().map(r -> r.getName()).toList());
	}

	public String generateRefreshToken(String username) throws NoSuchElementException {
		User user = userService.findUser(username);
		RefreshToken token;
		if (refreshTokenRepository.existsByOwner(user)) {
			token = refreshTokenRepository.findByOwner(user).orElseThrow(
					() -> new NoSuchElementException("No token found associeted with the " + username + " user."));
			if (token.getExpirationDate().compareTo(Instant.now()) < 0) {
				refreshTokenRepository.delete(token);
				return generateRefreshToken(user).getValue();
			} else {
				return token.getValue();
			}
		} else {
			return generateRefreshToken(user).getValue();
		}
	}

	private RefreshToken generateRefreshToken(User user) {
		RefreshToken token;
		do {
			token = RefreshToken.builder().value(UUID.randomUUID().toString())
					.expirationDate(Instant.now().plus(2, ChronoUnit.DAYS)).owner(user).build();
		} while (refreshTokenRepository.existsByValue(token.getValue()));
		refreshTokenRepository.save(token);
		return token;
	}

	public User validateRefreshToken(String tokenValue) throws NoSuchElementException, TokenExpiredException {
		RefreshToken token = refreshTokenRepository.findByValue(tokenValue)
				.orElseThrow(() -> new NoSuchElementException("No token found with the associeted value."));
		if (token.getExpirationDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(token);
			throw new TokenExpiredException("Given token was expired at:" + token.getExpirationDate().toString());
		}
		else
			return token.getOwner();
	}

	public void deleteRefreshToken(String tokenValue) throws NoSuchElementException {
		RefreshToken token = refreshTokenRepository.findByValue(tokenValue)
				.orElseThrow(() -> new NoSuchElementException("No token found with the associeted value."));
		refreshTokenRepository.delete(token);
	}
}
