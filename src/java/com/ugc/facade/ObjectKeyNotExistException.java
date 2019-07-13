package com.ugc.facade;

public class ObjectKeyNotExistException extends Exception
{
    public ObjectKeyNotExistException(String message){
        super(message);
    }
}
