package com.carecircle.auth_service.exception;

public class UserAlreadyExistsException extends RuntimeException{
	public UserAlreadyExistsException(String message) {
        super(message);
    }
}
