package com.ercoles.configserver.controllers;

public class ConfigurationNotFoundException extends Exception {
    public ConfigurationNotFoundException(String message) {
        super(message);
    }
}
