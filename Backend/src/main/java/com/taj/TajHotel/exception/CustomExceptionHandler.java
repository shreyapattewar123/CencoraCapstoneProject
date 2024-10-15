package com.taj.TajHotel.exception;

import com.taj.TajHotel.dto.ApiResponseDTO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponseDTO> handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {
        ApiResponseDTO response = new ApiResponseDTO();
        response.setStatusCode(HttpStatus.NOT_FOUND.value());
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ApiResponseDTO response = new ApiResponseDTO();
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ApiResponseDTO> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
        ApiResponseDTO response = new ApiResponseDTO();
        response.setStatusCode(HttpStatus.NOT_FOUND.value());
        response.setMessage("No booking found for the given ID");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO> handleGlobalException(Exception ex, WebRequest request) {
        ApiResponseDTO response = new ApiResponseDTO();
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage("An unexpected error occurred: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }




    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponseDTO> handleIOException(IOException ex, WebRequest request) {
        ApiResponseDTO response = new ApiResponseDTO();
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage("Error processing room photo: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponseDTO> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        ApiResponseDTO response = new ApiResponseDTO();
        response.setStatusCode(HttpStatus.NOT_FOUND.value());
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }



}
