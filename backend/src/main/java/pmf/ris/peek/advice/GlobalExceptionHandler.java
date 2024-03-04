package pmf.ris.peek.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(pmf.ris.peek.exceptions.Exception.class)
	public ResponseEntity<String> exceptionHandler(pmf.ris.peek.exceptions.Exception exc){
		return new ResponseEntity<String>(exc.getMessage(), exc.getStatus());
	}
}
