package com.ramsey.betterexamsrestapi.handler;

import com.ramsey.betterexamsrestapi.error.ResultNotFoundError;
import com.ramsey.betterexamsrestapi.pojo.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ExamResultExceptionHandler {

	@ExceptionHandler(ResultNotFoundError.class)
	public ResponseEntity<?> handleExamResultNotFoundError(Error error) {
		
		ErrorResponse errorResponse = new ErrorResponse(
				NOT_FOUND.value(),
				NOT_FOUND.getReasonPhrase(),
				error.getMessage()
		);
		return ResponseEntity.status(NOT_FOUND)
				.body(errorResponse);
		
	}
	
}
