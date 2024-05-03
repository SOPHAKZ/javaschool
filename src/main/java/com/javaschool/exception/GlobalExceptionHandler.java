package com.javaschool.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

//import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // global exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handlerGlobalException(Exception exception,
                                                                         WebRequest request){
        ErrorDetails errorDetails =
                new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.name(), exception.getMessage(),request.getDescription(false));

        return new ResponseEntity<>(errorDetails,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // specific exception
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDetails> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception,
                                                               WebRequest request){
        ErrorDetails errorDetails =
                new ErrorDetails(HttpStatus.BAD_REQUEST.name(), exception.getMessage(),request.getDescription(false));

        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> methodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                                            WebRequest request){
        ErrorDetails errorDetails =
                new ErrorDetails(HttpStatus.BAD_REQUEST.name(), exception.getMessage(),request.getDescription(false));

        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ErrorDetails> numberFormatException(NumberFormatException exception,
                                                                         WebRequest request){
        ErrorDetails errorDetails =
                new ErrorDetails(HttpStatus.BAD_REQUEST.name(),exception.getMessage(),request.getDescription(false));

        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDetails> constraintViolationException(ConstraintViolationException exception,
                                                                         WebRequest request){
        ErrorDetails errorDetails =
                new ErrorDetails(HttpStatus.BAD_REQUEST.name(),exception.getMessage(),request.getDescription(false));

        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> dataIntegrityViolationException(DataIntegrityViolationException exception,
                                                                         WebRequest request){
        ErrorDetails errorDetails =
                new ErrorDetails(HttpStatus.BAD_REQUEST.name(),exception.getMessage(),request.getDescription(false));

        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handlerResourceNotFoundException(ResourceNotFoundException exception,
                                                                         WebRequest request){
        ErrorDetails errorDetails =
                new ErrorDetails(HttpStatus.NOT_FOUND.name(),exception.getMessage(),request.getDescription(false));

        return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
    }



    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorDetails> handleApiException(ApiException e,WebRequest request){
        ErrorDetails errorDetails =
                new ErrorDetails(e.getStatus().name(),e.getMessage(),request.getDescription(false));
        return new ResponseEntity<>(errorDetails,e.getStatus());
    }


}
