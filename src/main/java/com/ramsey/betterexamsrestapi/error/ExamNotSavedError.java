package com.ramsey.betterexamsrestapi.error;

public class ExamNotSavedError extends Error {
	
	public ExamNotSavedError() {
		
		super("The exam was not saved successfully");
		
	}
	
	public ExamNotSavedError(String message) {
		
		super(message);
		
	}
	
}
