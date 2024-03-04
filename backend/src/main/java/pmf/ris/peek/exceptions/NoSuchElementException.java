package pmf.ris.peek.exceptions;

import org.springframework.http.HttpStatus;

public class NoSuchElementException extends Exception {

	private static final long serialVersionUID = 1L;

	public NoSuchElementException(String message) {
		super(message, HttpStatus.BAD_REQUEST);
	}

}
