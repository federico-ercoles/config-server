package com.ercoles.configserver.controllers;

import com.ercoles.configserver.dtos.ConfigurationRequestDto;
import com.ercoles.configserver.dtos.ConfigurationResponseDto;
import com.ercoles.configserver.dtos.ErrorDto;
import com.ercoles.configserver.services.ConfigService;
import com.ercoles.configserver.services.ConfigServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {
    private final ConfigService configService;

    public Controller(@Autowired ConfigService configService) {
        this.configService = configService;
    }

    @GetMapping("/")
    public List<ConfigurationResponseDto> getAll() throws ProcessingException {
        try {
            return configService.getAll();
        } catch (ConfigServiceException e) {
            throw new ProcessingException(e.getMessage() + " Caused by: " + e.getCause().getMessage());
        }
    }

    @GetMapping("/{configId}")
    public ConfigurationResponseDto getConfiguration(
            @PathVariable(name = "configId") String configId
    ) throws ConfigurationNotFoundException {
        try {
            return configService.getConfiguration(configId);
        } catch (ConfigServiceException e) {
            throw new ConfigurationNotFoundException(e.getMessage() + " Caused by: " + e.getCause().getMessage());
        }
    }

    @PostMapping("/{configId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ConfigurationResponseDto setConfiguration(
            @PathVariable(name = "configId") String configId,
            @RequestBody ConfigurationRequestDto configuration
    ) throws InvalidParametersException, ConfigurationAlreadyExistsException, ProcessingException {
        if (this.areParametersNotValid(configuration)) {
            throw new InvalidParametersException("Submitted configuration parameters are not valid.");
        }

        try {
            return configService.createConfiguration(configId, configuration.getName(), configuration.getValue());
        } catch (ConfigServiceException e) {
            Throwable cause = e.getCause();
            if (cause.getMessage().contains("already in use")) {
                throw new ConfigurationAlreadyExistsException(e.getMessage() + " Caused by: " + cause.getMessage());
            } else {
                throw new ProcessingException(e.getMessage() + " Caused by: " + cause.getMessage());
            }
        }
    }

    @PutMapping("/{configId}")
    public ConfigurationResponseDto updateConfiguration(
            @PathVariable(name = "configId") String configId,
            @RequestBody ConfigurationRequestDto configuration
    ) throws InvalidParametersException, ConfigurationNotFoundException, ProcessingException {
        if (this.areParametersNotValid(configuration)) {
            throw new InvalidParametersException("Submitted configuration parameters are not valid.");
        }

        try {
            return configService.updateConfiguration(configId, configuration.getName(), configuration.getValue());
        } catch (ConfigServiceException e) {
            Throwable cause = e.getCause();
            if (cause.getMessage().contains("No existing configuration")) {
                throw new ConfigurationNotFoundException(e.getMessage() + " Caused by: " + cause.getMessage());
            } else {
                throw new ProcessingException(e.getMessage() + " Caused by: " + cause.getMessage());
            }
        }
    }

    @DeleteMapping("/{configId}")
    public ConfigurationResponseDto deleteConfiguration(
            @PathVariable(name = "configId") String configId
    ) throws ConfigurationNotFoundException, ProcessingException {
        try {
            return configService.deleteConfiguration(configId);
        } catch (ConfigServiceException e) {
            Throwable cause = e.getCause();
            if (cause.getMessage().contains("No existing configuration")) {
                throw new ConfigurationNotFoundException(e.getMessage() + " Caused by: " + cause.getMessage());
            } else {
                throw new ProcessingException(e.getMessage() + " Caused by: " + cause.getMessage());
            }
        }
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidParametersException.class)
    public ErrorDto handleInvalidParametersException(Exception exception) {
        return new ErrorDto(exception.getMessage());
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ConfigurationNotFoundException.class)
    public ErrorDto handleConfigurationNotFoundException(Exception exception) {
        return new ErrorDto(exception.getMessage());
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(ConfigurationAlreadyExistsException.class)
    public ErrorDto handleConfigurationAlreadyExistsException(Exception exception) {
        return new ErrorDto(exception.getMessage());
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ProcessingException.class)
    public ErrorDto handleProcessingException(Exception exception) {
        return new ErrorDto(exception.getMessage());
    }

    private boolean areParametersNotValid(ConfigurationRequestDto configurationRequest) {
        boolean isNameNotValid = configurationRequest.getName() == null || configurationRequest.getName().isBlank();
        boolean isValueNotValid = configurationRequest.getValue() == null || configurationRequest.getValue().isBlank();
        return isNameNotValid || isValueNotValid;
    }
}
