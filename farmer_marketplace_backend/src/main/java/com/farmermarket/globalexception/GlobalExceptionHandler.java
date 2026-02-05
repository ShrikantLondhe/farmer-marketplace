package com.farmermarket.globalexception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.farmermarket.dto.common.ApiErrorResponse;
import com.farmermarket.exception.BadRequestException;
import com.farmermarket.exception.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ---------- 403 FORBIDDEN ----------
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(
            AccessDeniedException ex) {

        ApiErrorResponse error = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                "You are not allowed to access this resource"
        );

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    // ---------- 404 NOT FOUND ----------
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex) {

        ApiErrorResponse error = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Resource Not Found",
                ex.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // ---------- 400 BAD REQUEST ----------
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(
            BadRequestException ex) {

        ApiErrorResponse error = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // ---------- 500 INTERNAL SERVER ERROR ----------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAllExceptions(
            Exception ex) {

        ApiErrorResponse error = new ApiErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                ex.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
