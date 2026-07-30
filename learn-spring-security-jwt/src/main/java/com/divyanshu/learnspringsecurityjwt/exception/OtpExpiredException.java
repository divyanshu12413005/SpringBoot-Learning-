package com.divyanshu.learnspringsecurityjwt.exception;

public class OtpExpiredException extends RuntimeException {

    public OtpExpiredException(String message) {
        super(message);
    }
}
