package com.mysite.weatherviewer.exception;

public class InvalidUserDataException extends RuntimeException {
    public InvalidUserDataException(String message) {
        super(message);
    }
}
