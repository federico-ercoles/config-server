package com.ercoles.configserver.services;

public class ConfigServiceException extends Exception {
    public ConfigServiceException(String message) {
        super(message);
    }
    public ConfigServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
