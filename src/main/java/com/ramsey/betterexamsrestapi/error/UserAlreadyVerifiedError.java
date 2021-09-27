package com.ramsey.betterexamsrestapi.error;

public class UserAlreadyVerifiedError extends Error {
	
	public UserAlreadyVerifiedError(String username) {
		
		super(String.format("User %s is already verified", username));
		
	}
	
}
