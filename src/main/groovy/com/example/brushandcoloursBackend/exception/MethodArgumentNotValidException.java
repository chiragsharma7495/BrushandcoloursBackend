package com.example.brushandcoloursBackend.exception;

public class MethodArgumentNotValidException extends RuntimeException{
    public MethodArgumentNotValidException(String message){
        super(message);
    }
}
