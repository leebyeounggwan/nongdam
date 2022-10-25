package com.example.nongdam.exception;

public class EmailConfirmException extends CustomException {
    public EmailConfirmException(String m, String field) {
        super(m, field);
    }
}