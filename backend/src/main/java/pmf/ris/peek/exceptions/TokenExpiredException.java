package pmf.ris.peek.exceptions;

import org.springframework.http.HttpStatus;

public class TokenExpiredException extends Exception {

	private static final long serialVersionUID = 1L;

	public TokenExpiredException(String message) {
		super(message, HttpStatus.UNAUTHORIZED);
	}

}
