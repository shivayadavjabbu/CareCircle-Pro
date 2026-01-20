package com.parentpulse.auth.exception;

/*
 * Thrown when attempting to register a user. 
 * With an email that already exists
 */
public class UserAlreadyExistsException extends RuntimeException{

	public UserAlreadyExistsException(String message) {
		super(message); 
	}
}
