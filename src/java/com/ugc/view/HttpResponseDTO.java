package com.ugc.view;

import java.util.Map;
import java.util.HashMap;

public class HttpResponseDTO
{

    private int _status;
    private String _message;
    private boolean _errorOccured;
    private Map<String, String> _headers;


    public HttpResponseDTO() {
        this(200, null, false);
    }

    public HttpResponseDTO(int code) {
        this(code, null, false);
    }

    public HttpResponseDTO(int code, String message) {
        this(code, message, false);
    }

    public HttpResponseDTO(int status, String message, boolean errorOccured) {
        _status = status;
        _message = message;
        _errorOccured = errorOccured;
        _headers = new HashMap<String, String>();
    }

    public int getStatus() {
        return _status;
    }

    public void setStatus(int status) {
        _status = status;
    }

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        _message = message;
    }

    public boolean isErrorOccured() {
        return _errorOccured;
    }

    public void setErrorOccured(boolean errorOccured) {
        _errorOccured = errorOccured;
    }

    public void addHeader(String key, String value) {
        _headers.put(key, value);
    }

    public Map<String, String> getHeaders() {
        return _headers;
    }
}


