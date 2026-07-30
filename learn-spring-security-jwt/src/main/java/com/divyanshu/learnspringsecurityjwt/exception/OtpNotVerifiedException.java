package com.divyanshu.learnspringsecurityjwt.exception;

public class OtpNotVerifiedException extends RuntimeException {

    public OtpNotVerifiedException(String message) {
        super(message);
    }
}