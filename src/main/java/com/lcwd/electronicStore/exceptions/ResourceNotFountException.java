package com.lcwd.electronicStore.exceptions;

import lombok.Builder;

@Builder
public class ResourceNotFountException extends RuntimeException{
    public ResourceNotFountException(){
        super("Resource Not Found!!");
    }
    public ResourceNotFountException(String message){
        super(message);
    }
}
