package com.parentpulse.auth.exception;

/*
 * Thrown when authentication credentials are invalid.
 */
public class InvalidCredentialsException extends RuntimeException{

	public InvalidCredentialsException(String message) {
		super(message); 
	}
}
