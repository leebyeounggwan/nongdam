package com.example.nongdam.global.exception;

public class AuthenticationException extends CustomException{

    public AuthenticationException(String m, String field) {
        super(m, field);
    }
}
