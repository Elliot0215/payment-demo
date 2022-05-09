package com.eryue.paymentdemo.exception;

import com.eryue.paymentdemo.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BuessExcetion.class)
    @ResponseBody
    public Result handleIllegalArgumentPlumException(BuessExcetion e) {
        String message = e.getMessage();
        return new Result().setMessage(message);
    }

}
