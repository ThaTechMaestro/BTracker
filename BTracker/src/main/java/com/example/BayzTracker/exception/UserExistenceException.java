package com.example.BayzTracker.exception;

public class UserExistenceException extends RuntimeException{

    public UserExistenceException(String message){
        super(message);
    }
}
