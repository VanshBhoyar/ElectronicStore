package com.lcwd.electronicStore.exceptions;

public class BadApiRequestException extends RuntimeException{
    public BadApiRequestException(){
        super("Bad Api Request!!");
    }
    public BadApiRequestException(String message){
        super(message);
    }
}
