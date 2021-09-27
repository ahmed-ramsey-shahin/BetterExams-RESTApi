package com.ramsey.betterexamsrestapi.error;

public class VerificationCodeNotCorrectError extends Error {

	public VerificationCodeNotCorrectError() {
		
		super("The verification code is not correct");
		
	}

}
