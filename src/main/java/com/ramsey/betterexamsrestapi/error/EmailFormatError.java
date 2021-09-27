package com.ramsey.betterexamsrestapi.error;

public class EmailFormatError extends Error {
	
	public EmailFormatError() {
		
		super("The given email is not in its appropriate format");
		
	}
	
}
