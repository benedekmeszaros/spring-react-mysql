package pmf.ris.peek.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import lombok.SneakyThrows;
import pmf.ris.peek.security.TokenAuthenticationFilter;
import pmf.ris.peek.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private static final String AUTHORITY_PREFIX = "ROLE_";
	private static final String USER = AUTHORITY_PREFIX + "USER";
	private static final String ADMIN = AUTHORITY_PREFIX + "ADMIN";
	
    @Bean
    AuthenticationManager authManager(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {

        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authProvider);
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
     
    @Bean
    UserDetailsService userDetailsService() {
    	return new UserService();
    }
    
	@Bean
	@SneakyThrows
	KeyPair keypair() {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("rsa");
			keyPairGenerator.initialize(2048);
			return keyPairGenerator.generateKeyPair();
	}
	
	@Bean
	JwtEncoder encoder(KeyPair keypair) {
		JWK jwk = new RSAKey.Builder((RSAPublicKey) keypair.getPublic()).privateKey(keypair.getPrivate()).build();
		JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(jwks);
	}
	
	@Bean
	JwtDecoder decoder(KeyPair keypair) {
		return NimbusJwtDecoder.withPublicKey((RSAPublicKey) keypair.getPublic()).build();
	}
    
	@Bean 
	TokenAuthenticationFilter tokenAuthenticationFilter() {
		return new TokenAuthenticationFilter();
	}
	
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	return http
				.csrf(csrf -> csrf.disable())
				.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(requests -> requests
						.requestMatchers("/auth/**").permitAll()
						.requestMatchers("/public/**").permitAll()
						.requestMatchers("/private/user/**").hasAuthority(USER)
						.requestMatchers("/private/admin/**").hasAuthority(ADMIN)
						.anyRequest().authenticated())
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> 
				jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))	
				.build();
    }
    
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(AUTHORITY_PREFIX);

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }
}
