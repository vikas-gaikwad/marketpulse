package com.marketpulse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
//"This class will globally handle exceptions thrown by REST controllers."
//Controller 1 ─┐
//Controller 2 ─┤
//Controller 3 ─┼──→ GlobalExceptionHandler
//Controller 4 ─┘
public class GlobalExceptionHandler {
    @ExceptionHandler(StockNotFoundException.class)
    //When a StockNotFoundException occurs, call this method.
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleStockNotFound(StockNotFoundException stockNotFoundException){
        return stockNotFoundException.getMessage();
    }
}
