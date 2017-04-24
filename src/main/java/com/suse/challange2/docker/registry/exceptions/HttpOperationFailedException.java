package com.suse.challange2.docker.registry.exceptions;

public class HttpOperationFailedException extends RuntimeException {

    public HttpOperationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}