package com.ercoles.configserver.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.io.Serializable;

@Value
public class ConfigurationRequestDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("name")
    String name;
    @JsonProperty("value")
    String value;
}
