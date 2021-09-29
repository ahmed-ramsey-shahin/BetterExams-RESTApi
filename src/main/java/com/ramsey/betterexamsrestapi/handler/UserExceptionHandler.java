package com.ramsey.betterexamsrestapi.handler;

import com.ramsey.betterexamsrestapi.error.*;
import com.ramsey.betterexamsrestapi.pojo.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class UserExceptionHandler {
	
	@ExceptionHandler({
			UserAlreadyExistsError.class,
			UserAlreadyVerifiedError.class,
			VerificationCodeNotCorrectError.class
	})
	public ResponseEntity<?> forbiddenStatusErrors(Error err) {
		
		ErrorResponse errorResponse = new ErrorResponse(
				FORBIDDEN.value(),
				FORBIDDEN.getReasonPhrase(),
				err.getMessage()
		);
		return ResponseEntity.status(FORBIDDEN)
				.body(errorResponse);
		
	}
	
	@ExceptionHandler({AccessDeniedException.class, UsernameNotFoundException.class})
	public ResponseEntity<?> forbiddenStatusException(Exception ex) {
		
		ErrorResponse errorResponse = new ErrorResponse(
				FORBIDDEN.value(),
				FORBIDDEN.getReasonPhrase(),
				ex.getMessage()
		);
		return ResponseEntity.status(FORBIDDEN)
				.body(errorResponse);
		
	}
	
	@ExceptionHandler(UserNotFoundError.class)
	public ResponseEntity<?> notFoundStatusErrors(Error err) {
		
		ErrorResponse errorResponse = new ErrorResponse(
				NOT_FOUND.value(),
				NOT_FOUND.getReasonPhrase(),
				err.getMessage()
		);
		return ResponseEntity.status(NOT_FOUND)
				.body(errorResponse);
		
	}
	
}
