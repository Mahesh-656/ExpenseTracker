package org.expensetracker.exception;

import org.expensetracker.dto.ResponseStructure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MissingFieldsException.class)
    public ResponseEntity<ResponseStructure<String>> handleMissingFieldsException(MissingFieldsException exception){
        ResponseStructure<String> res=new ResponseStructure<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setMessage("failure");
        res.setData(exception.getMessage());
        return new ResponseEntity<>(res,HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(NoResultFoundException.class)
    public ResponseEntity<ResponseStructure<String>> handleNoResultFoundException(NoResultFoundException exception){
        ResponseStructure<String> res=new ResponseStructure<>();
        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setMessage("failure");
        res.setData(exception.getMessage());
        return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);

    }
}
