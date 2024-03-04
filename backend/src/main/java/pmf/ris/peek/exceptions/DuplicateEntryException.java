package pmf.ris.peek.exceptions;

import org.springframework.http.HttpStatus;

public class DuplicateEntryException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public DuplicateEntryException(String message) {
		super(message, HttpStatus.FORBIDDEN);
	}


}
