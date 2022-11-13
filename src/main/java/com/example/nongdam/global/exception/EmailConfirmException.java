package com.example.nongdam.global.exception;

public class EmailConfirmException extends CustomException {
    public EmailConfirmException(String m, String field) {
        super(m, field);
    }
}