package org.expensetracker.exception;


public class MissingFieldsException extends RuntimeException{

    public MissingFieldsException(String message){
        super(message);
    }
}
