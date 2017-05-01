package com.suse.challange2.docker.registry.exceptions;

public class NetworkException extends RuntimeException {

    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
