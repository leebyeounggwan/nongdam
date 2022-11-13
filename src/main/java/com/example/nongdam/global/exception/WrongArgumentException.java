package com.example.nongdam.global.exception;


public class WrongArgumentException extends CustomException{

    public WrongArgumentException(String m, String field) {
        super(m, field);
    }
}
