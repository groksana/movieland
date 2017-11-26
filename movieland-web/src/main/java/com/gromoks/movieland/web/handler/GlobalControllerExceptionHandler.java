package com.gromoks.movieland.web.handler;

import com.gromoks.movieland.web.entity.ExceptionDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.naming.AuthenticationException;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleException(IllegalArgumentException e) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setErrorCode(HttpStatus.BAD_REQUEST);
        exceptionDto.setMessage(e.getMessage());
        return exceptionDto;
    }

    @ExceptionHandler(SecurityException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionDto handleException(SecurityException e) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setErrorCode(HttpStatus.FORBIDDEN);
        exceptionDto.setMessage(e.getMessage());
        return exceptionDto;
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionDto handleException(AuthenticationException e) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setErrorCode(HttpStatus.UNAUTHORIZED);
        exceptionDto.setMessage(e.getMessage());
        return exceptionDto;
    }
}
