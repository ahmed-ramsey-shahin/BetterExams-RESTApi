package com.ramsey.betterexamsrestapi.handler;

import com.ramsey.betterexamsrestapi.error.PasswordFormatError;
import com.ramsey.betterexamsrestapi.pojo.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestControllerAdvice
public class ValidationExceptionHandler {
	
	@ExceptionHandler({
			PasswordFormatError.class,
			
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
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex) {
		
		StringBuilder message = new StringBuilder();
		message.append("[");
		ex.getConstraintViolations()
				.forEach(
						constraintViolation -> {
							
							message.append("{");
							message.append(constraintViolation.getPropertyPath());
							message.append(", ");
							message.append(constraintViolation.getMessage());
							message.append("}");
							
						}
				);
		message.append("]");
		ErrorResponse errorResponse = new ErrorResponse(
				FORBIDDEN.value(),
				FORBIDDEN.getReasonPhrase(),
				message.toString()
		);
		return ResponseEntity.status(FORBIDDEN)
				.body(errorResponse);
		
	}
	
}
