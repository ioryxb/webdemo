package com.crxdemo.ticketoffice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object logicExceptionHandler(HttpServletRequest request, Exception e, HttpServletResponse response) {
        ResponseEntity<Object> result = null;
        if (e instanceof LogicException) {
            LogicException logicException = (LogicException) e;
            result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(((LogicException) e).getCode() + "  customered message:" + ((LogicException) e).getErrorMsg());
        } else {
            result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        logger.error("exception in advice:" + e.getMessage(), e);
        return result;
    }
}
