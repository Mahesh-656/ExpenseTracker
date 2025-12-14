package org.expensetracker.exception;

public class NoResultFoundException extends RuntimeException{

    public NoResultFoundException(String message){
        super(message);
    }
}
