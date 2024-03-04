package pmf.ris.peek.exceptions;

import org.springframework.http.HttpStatus;

public class UnsupportedOperationException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public UnsupportedOperationException(String message) {
		super(message, HttpStatus.FORBIDDEN);
	}


}
