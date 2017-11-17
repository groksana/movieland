package com.gromoks.movieland.web.controller;

import com.gromoks.movieland.web.entity.ExceptionDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity handleException(IllegalArgumentException e) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setErrorCode(HttpStatus.BAD_REQUEST);
        exceptionDto.setMessage(e.getMessage());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        ResponseEntity responseEntity = new ResponseEntity(exceptionDto,httpHeaders,HttpStatus.BAD_REQUEST);
        return responseEntity;
    }

}
