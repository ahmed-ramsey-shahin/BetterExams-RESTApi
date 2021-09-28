package com.ramsey.betterexamsrestapi.error;

public class ExamNotFoundError extends Error {
	
	public ExamNotFoundError(Long id) {
		
		super(String.format("Exam %d was not found", id));
		
	}
	
}
