package pmf.ris.peek.security;

import java.io.IOException;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter  {

	@Autowired
	private JwtDecoder jwtDecoder;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authorizationHeader = request.getHeader("Authorization");
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			try {
				String token = authorizationHeader.substring(7);
				Jwt jwt = jwtDecoder.decode(token);
				if(jwt.getExpiresAt().compareTo(Instant.now())< 0) {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		            return;			
				}
			} catch (Exception e) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            return;	
			}
		}
        filterChain.doFilter(request, response);
	}

}
