package com.money.money.global.exception;

public class MissingAuthHeaderException extends RuntimeException{
    public MissingAuthHeaderException(String message) {
        super(message);
    }
}
