package com.divyanshu.learnspringsecurityjwt.exception;


public class OtpRequestLimitExceededException extends RuntimeException {

    public OtpRequestLimitExceededException(String message) {
        super(message);
    }
}