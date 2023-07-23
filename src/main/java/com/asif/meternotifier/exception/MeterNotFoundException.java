package com.asif.meternotifier.exception;

public class MeterNotFoundException extends RuntimeException {
    public MeterNotFoundException(String message) {
        super(message);
    }
}
