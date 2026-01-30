package com.carecircle.auth_service.loginRegister.exception;

public class UserAlreadyExistsException extends RuntimeException{
	public UserAlreadyExistsException(String message) {
        super(message);
    }
}
