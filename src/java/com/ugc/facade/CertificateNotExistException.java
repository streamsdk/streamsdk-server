package com.ugc.facade;

public class CertificateNotExistException extends Exception
{

    public CertificateNotExistException(String message){
        super(message);
    }

}
