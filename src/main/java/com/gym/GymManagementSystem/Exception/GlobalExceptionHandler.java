package com.gym.GymManagementSystem.Exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.gym.GymManagementSystem.DTO.Response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
        MethodArgumentNotValidException ex ,
        HttpServletRequest request
    ){
        Map<String,String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));
        ErrorResponse response = new ErrorResponse();
        response.setTimeStamp(LocalDateTime.now());
        response.setStatus(400);
        response.setError("Bad Request");
        response.setMessage("Validation Failed");
        response.setPath(request.getRequestURI());
        response.setFieldError(fieldErrors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AccountDisabledException.class)
    public ResponseEntity <ErrorResponse> handleAccountDisabledException(
        AccountDisabledException ex ,
        HttpServletRequest request
    ){
        ErrorResponse response = new ErrorResponse();
        response.setTimeStamp(LocalDateTime.now());
        response.setStatus(403);
        response.setError("Forbidden");
        response.setMessage(ex.getMessage());
        response.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(
        DuplicateResourceException ex ,
        HttpServletRequest request
    ){
        ErrorResponse response = new ErrorResponse();
        response.setTimeStamp(LocalDateTime.now());
        response.setStatus(409);
        response.setError("Conflict");
        response.setMessage(ex.getMessage());
        response.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(
        InvalidCredentialsException ex ,
        HttpServletRequest request
    ){
        ErrorResponse response = new ErrorResponse();
        response.setTimeStamp(LocalDateTime.now());
        response.setStatus(401);
        response.setError("Unauthorized");
        response.setMessage(ex.getMessage());
        response.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(ResourceAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleResourceAccessDeniedException(
        ResourceAccessDeniedException ex ,
        HttpServletRequest request
    ){
        ErrorResponse response = new ErrorResponse();
        response.setTimeStamp(LocalDateTime.now());
        response.setStatus(403);
        response.setError("Forbidden");
        response.setMessage(ex.getMessage());
        response.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleCustomValidationException(
        ValidationException ex ,
        HttpServletRequest request
    ){
        ErrorResponse response = new ErrorResponse();
        response.setTimeStamp(LocalDateTime.now());
        response.setStatus(400);
        response.setError("Bad Request");
        response.setMessage(ex.getMessage());
        response.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException ex ,
        HttpServletRequest request
    ){
        ErrorResponse response = new ErrorResponse();
        response.setTimeStamp(LocalDateTime.now());
        response.setStatus(400);
        response.setError("Bad Request");
        response.setMessage("Invalid Request Body or Unsupported field value");
        response.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
        DataIntegrityViolationException ex ,
        HttpServletRequest request
    ){
        ErrorResponse response = new ErrorResponse();
        response.setTimeStamp(LocalDateTime.now());
        response.setStatus(409);
        response.setError("Conflict");
        response.setMessage("A database Constraint was violated");
        response.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }


    
}
