package com.ramsey.betterexamsrestapi.error;

public class PasswordFormatError extends Error {
	
	public PasswordFormatError() {
		
		super("The password does not match the specified format");
		
	}
	
}
