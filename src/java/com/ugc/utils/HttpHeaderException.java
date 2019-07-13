package com.ugc.utils;

public class HttpHeaderException extends Exception{

    public HttpHeaderException() {
        super();
    }

    public HttpHeaderException(String message) {
        super(message);
    }

    public HttpHeaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpHeaderException(Throwable cause) {
        super(cause);
    }

}
