package com.carecircle.user_profile_service.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.carecircle.user_profile_service.admin.exception.*;
import com.carecircle.user_profile_service.caregiver.exception.*;

/**
 * Centralized exception handling for the user-profile-service.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // =========================
    // Validation Errors (400)
    // =========================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
            MethodArgumentNotValidException ex
    ) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        return ResponseEntity
                .badRequest()
                .body(new ApiError(message, HttpStatus.BAD_REQUEST.value()));
    }

    // =========================
    // Not Found (404)
    // =========================
    @ExceptionHandler({
            CaregiverProfileNotFoundException.class,
            CaregiverCapabilityNotFoundException.class,
            CaregiverCertificationNotFoundException.class,
            VerificationTargetNotFoundException.class,
            AdminProfileNotFoundException.class
    })
    public ResponseEntity<ApiError> handleNotFound(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    // =========================
    // Forbidden (403)
    // =========================
    @ExceptionHandler({
            AdminInactiveException.class,
            AdminAccessDeniedException.class
    })
    public ResponseEntity<ApiError> handleForbidden(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ApiError(ex.getMessage(), HttpStatus.FORBIDDEN.value()));
    }

    // =========================
    // Bad Request (400)
    // =========================
    @ExceptionHandler({
            InvalidVerificationStateException.class,
            CityNotFoundException.class
    })
    public ResponseEntity<ApiError> handleBadRequest(RuntimeException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ApiError(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    // =========================
    // Fallback (500)
    // =========================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnhandledException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError(
                        "Internal server error",
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                ));
    }
}
