package com.ramsey.betterexamsrestapi.error;

public class UserNotFoundError extends Error {
	
	public UserNotFoundError(String username) {
		
		super(String.format("User %s was not found", username));
		
	}
	
}
