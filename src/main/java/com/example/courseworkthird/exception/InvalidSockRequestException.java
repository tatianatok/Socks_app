package com.example.courseworkthird.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Недопустимый запрос")
    public class InvalidSockRequestException extends RuntimeException{
        public InvalidSockRequestException(String message) {
            super(message);
    }
}