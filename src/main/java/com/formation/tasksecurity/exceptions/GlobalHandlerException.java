package com.formation.tasksecurity.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ResponseHandler> handleException(Exception e) {
        ResponseHandler response = new ResponseHandler();
        response.status = HttpStatus.BAD_REQUEST.value();
        response.errors = List.of(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ResponseHandler> handleValidationException(MethodArgumentNotValidException e) {

        List<Map<String, String>> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> Map.of(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

//        List<String> globalErrors = e.getBindingResult().getGlobalErrors().stream()
//                .map(ObjectError::getDefaultMessage)
//                .collect(Collectors.toList());

        ResponseHandler response = new ResponseHandler();
        response.status = HttpStatus.NOT_ACCEPTABLE.value();
        response.errors = fieldErrors;
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }
}
