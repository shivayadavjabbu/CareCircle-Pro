package com.carecircle.auth_service.loginRegister.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Global exception handler for the auth-service.
 * Provides comprehensive logging and consistent error responses.
 * All exceptions are logged with context for easy debugging and tracking.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle UserAlreadyExistsException - HTTP 409 Conflict
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(
            UserAlreadyExistsException ex, 
            WebRequest request) {
        
        logger.warn("User already exists - ErrorCode: {}, Message: {}, Path: {}", 
                ex.getErrorCode(), 
                ex.getMessage(), 
                request.getDescription(false));
        
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ex.getErrorCode(),
                ex.getHttpStatus().value(),
                extractPath(request)
        );
        
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    /**
     * Handle InvalidCredentialsException - HTTP 401 Unauthorized
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(
            InvalidCredentialsException ex, 
            WebRequest request) {
        
        logger.warn("Invalid credentials - ErrorCode: {}, Message: {}, Path: {}", 
                ex.getErrorCode(), 
                ex.getMessage(), 
                request.getDescription(false));
        
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ex.getErrorCode(),
                ex.getHttpStatus().value(),
                extractPath(request)
        );
        
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    /**
     * Handle UserNotFoundException - HTTP 404 Not Found
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
            UserNotFoundException ex, 
            WebRequest request) {
        
        logger.warn("User not found - ErrorCode: {}, Message: {}, Path: {}", 
                ex.getErrorCode(), 
                ex.getMessage(), 
                request.getDescription(false));
        
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ex.getErrorCode(),
                ex.getHttpStatus().value(),
                extractPath(request)
        );
        
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    /**
     * Handle AccountNotVerifiedException - HTTP 403 Forbidden
     */
    @ExceptionHandler(AccountNotVerifiedException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotVerified(
            AccountNotVerifiedException ex, 
            WebRequest request) {
        
        logger.warn("Account not verified - ErrorCode: {}, Message: {}, Path: {}", 
                ex.getErrorCode(), 
                ex.getMessage(), 
                request.getDescription(false));
        
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ex.getErrorCode(),
                ex.getHttpStatus().value(),
                extractPath(request)
        );
        
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    /**
     * Handle OtpExpiredException - HTTP 400 Bad Request
     */
    @ExceptionHandler(OtpExpiredException.class)
    public ResponseEntity<ErrorResponse> handleOtpExpired(
            OtpExpiredException ex, 
            WebRequest request) {
        
        logger.warn("OTP expired - ErrorCode: {}, Message: {}, Path: {}", 
                ex.getErrorCode(), 
                ex.getMessage(), 
                request.getDescription(false));
        
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ex.getErrorCode(),
                ex.getHttpStatus().value(),
                extractPath(request)
        );
        
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    /**
     * Handle OtpNotFoundException - HTTP 404 Not Found
     */
    @ExceptionHandler(OtpNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOtpNotFound(
            OtpNotFoundException ex, 
            WebRequest request) {
        
        logger.warn("OTP not found - ErrorCode: {}, Message: {}, Path: {}", 
                ex.getErrorCode(), 
                ex.getMessage(), 
                request.getDescription(false));
        
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ex.getErrorCode(),
                ex.getHttpStatus().value(),
                extractPath(request)
        );
        
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    /**
     * Handle InvalidOtpException - HTTP 400 Bad Request
     */
    @ExceptionHandler(InvalidOtpException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOtp(
            InvalidOtpException ex, 
            WebRequest request) {
        
        logger.warn("Invalid OTP - ErrorCode: {}, Message: {}, Path: {}, RemainingAttempts: Check logs", 
                ex.getErrorCode(), 
                ex.getMessage(), 
                request.getDescription(false));
        
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ex.getErrorCode(),
                ex.getHttpStatus().value(),
                extractPath(request)
        );
        
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    /**
     * Handle TooManyOtpAttemptsException - HTTP 429 Too Many Requests
     */
    @ExceptionHandler(TooManyOtpAttemptsException.class)
    public ResponseEntity<ErrorResponse> handleTooManyOtpAttempts(
            TooManyOtpAttemptsException ex, 
            WebRequest request) {
        
        logger.warn("Too many OTP attempts - ErrorCode: {}, Message: {}, Path: {}", 
                ex.getErrorCode(), 
                ex.getMessage(), 
                request.getDescription(false));
        
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ex.getErrorCode(),
                ex.getHttpStatus().value(),
                extractPath(request)
        );
        
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    /**
     * Handle all other AuthServiceException subclasses - Fallback handler
     */
    @ExceptionHandler(AuthServiceException.class)
    public ResponseEntity<ErrorResponse> handleAuthServiceException(
            AuthServiceException ex, 
            WebRequest request) {
        
        logger.error("Auth service exception - ErrorCode: {}, Message: {}, Path: {}", 
                ex.getErrorCode(), 
                ex.getMessage(), 
                request.getDescription(false), 
                ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                ex.getErrorCode(),
                ex.getHttpStatus().value(),
                extractPath(request)
        );
        
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    /**
     * Handle all unexpected exceptions - HTTP 500 Internal Server Error
     * This is critical for catching bugs and unexpected errors.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, 
            WebRequest request) {
        
        logger.error("Unexpected error occurred - Path: {}, Exception: {}", 
                request.getDescription(false), 
                ex.getClass().getSimpleName(), 
                ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
                "An unexpected error occurred. Please try again later.",
                "INTERNAL_SERVER_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                extractPath(request)
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Extract the request path from WebRequest description.
     * Format: "uri=/api/auth/login"
     */
    private String extractPath(WebRequest request) {
        String description = request.getDescription(false);
        if (description != null && description.startsWith("uri=")) {
            return description.substring(4);
        }
        return description;
    }
}
