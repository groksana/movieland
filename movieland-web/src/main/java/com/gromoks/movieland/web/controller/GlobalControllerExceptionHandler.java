package com.gromoks.movieland.web.controller;

import com.gromoks.movieland.web.entity.ExceptionDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<?> handleException(IllegalArgumentException e) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setErrorCode(HttpStatus.BAD_REQUEST);
        exceptionDto.setMessage(e.getMessage());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

        ResponseEntity<ExceptionDto> responseEntity = new ResponseEntity<ExceptionDto>(exceptionDto,httpHeaders,HttpStatus.BAD_REQUEST);
        return responseEntity;
    }

}
