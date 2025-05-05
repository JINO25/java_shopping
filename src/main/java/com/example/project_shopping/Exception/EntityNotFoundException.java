package com.example.project_shopping.Exception;

public class EntityNotFoundException extends RuntimeException{
    public  EntityNotFoundException(String message){
        super(message);
    }
}
