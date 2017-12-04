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
import java.sql.SQLException;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleException(IllegalArgumentException e) {
        return getExceptionDto(HttpStatus.BAD_REQUEST,e);
    }

    @ExceptionHandler(SecurityException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionDto handleException(SecurityException e) {
        return getExceptionDto(HttpStatus.FORBIDDEN,e);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionDto handleException(AuthenticationException e) {
        return getExceptionDto(HttpStatus.UNAUTHORIZED,e);
    }

    @ExceptionHandler(SQLException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleException(SQLException e) {
        return getExceptionDto(HttpStatus.BAD_REQUEST,e);
    }

    private ExceptionDto getExceptionDto(HttpStatus httpStatus, Exception e) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setErrorCode(httpStatus);
        exceptionDto.setMessage(e.getMessage());
        return exceptionDto;
    }
}
