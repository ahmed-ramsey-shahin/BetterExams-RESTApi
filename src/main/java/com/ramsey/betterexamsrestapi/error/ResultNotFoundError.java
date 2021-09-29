package com.ramsey.betterexamsrestapi.error;

public class ResultNotFoundError extends Error {
	
	public ResultNotFoundError(Long id) {
		
		super(String.format("Result %d was not found", id));
		
	}
	
}
