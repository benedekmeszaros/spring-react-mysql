package pmf.ris.peek.exceptions;

import org.springframework.http.HttpStatus;

public abstract class Exception extends java.lang.Exception {

	private static final long serialVersionUID = 1L;
	private HttpStatus status;
	
	public Exception(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}
	
	public HttpStatus getStatus() {
		return status;
	}
}
