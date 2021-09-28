package com.ramsey.betterexamsrestapi.handler;

import com.ramsey.betterexamsrestapi.error.ExamNotFoundError;
import com.ramsey.betterexamsrestapi.pojo.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ExamExceptionHandler {
	
	@ExceptionHandler(ExamNotFoundError.class)
	public ResponseEntity<?> handleExamNotFoundErrpr(ExamNotFoundError err) {
		
		ErrorResponse errorResponse = new ErrorResponse(
				NOT_FOUND.value(),
				NOT_FOUND.getReasonPhrase(),
				err.getMessage()
		);
		return ResponseEntity.status(NOT_FOUND)
				.body(errorResponse);
		
	}
	
}
