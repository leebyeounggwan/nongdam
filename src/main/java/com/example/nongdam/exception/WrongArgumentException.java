package com.example.nongdam.exception;


public class WrongArgumentException extends CustomException{

    public WrongArgumentException(String m, String field) {
        super(m, field);
    }
}
