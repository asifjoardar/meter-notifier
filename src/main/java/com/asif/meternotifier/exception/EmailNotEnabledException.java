package com.asif.meternotifier.exception;

import lombok.Getter;

@Getter
public class EmailNotEnabledException extends RuntimeException {
    private final Long id;

    public EmailNotEnabledException(String message, Long id) {
        super(message);
        this.id = id;
    }
}
