package com.shabadoit.exceptions;

public class SpellManagementException extends RuntimeException {

    public SpellManagementException(final String message) {
        super(message);
    }

    public SpellManagementException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
