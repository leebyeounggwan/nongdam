package com.example.nongdam.exception;

public class AuthenticationException extends CustomException{

    public AuthenticationException(String m, String field) {
        super(m, field);
    }
}
