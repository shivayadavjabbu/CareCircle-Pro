package com.parentpulse.auth.exception;

/*
 * Thrown when a user cannot be found.
 */
public class UserNotFoundException extends RuntimeException{

	public UserNotFoundException(String message) {
		super(message); 
	}
}
