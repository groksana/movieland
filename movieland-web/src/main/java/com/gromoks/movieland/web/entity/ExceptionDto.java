package com.gromoks.movieland.web.entity;

import org.springframework.http.HttpStatus;

public class ExceptionDto {
    private HttpStatus errorCode;
    private String message;

    public HttpStatus getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(HttpStatus errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
