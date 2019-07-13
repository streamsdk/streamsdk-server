package com.ugc.rest;

public class RestUrlPathMatcherException extends RuntimeException
{
    public RestUrlPathMatcherException(String message) {
        super(message);
    }


    public RestUrlPathMatcherException(String message, Throwable cause) {
        super(message, cause);
    }
}

