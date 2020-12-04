package com.ercoles.configserver.controllers;

public class ConfigurationAlreadyExistsException extends Exception {
    public ConfigurationAlreadyExistsException(String message) {
        super(message);
    }
}
