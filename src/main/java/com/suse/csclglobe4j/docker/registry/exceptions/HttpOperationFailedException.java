package com.suse.csclglobe4j.docker.registry.exceptions;

public class HttpOperationFailedException extends RuntimeException {

    private Integer statusCode;

    public HttpOperationFailedException(Integer statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public Integer getStatusCode() {
        return statusCode;
    }
}