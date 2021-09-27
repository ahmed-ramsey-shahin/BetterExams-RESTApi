package com.ramsey.betterexamsrestapi.error;

public class UserAlreadyExistsError extends Error {
	
	public UserAlreadyExistsError() {
		
		super("Username, or email already exists");
		
	}
	
}
