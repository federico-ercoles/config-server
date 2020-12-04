package com.ercoles.configserver.dtos;

import com.ercoles.configserver.repositories.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    String id;
    @JsonProperty("name")
    String name;
    @JsonProperty("value")
    String value;

    public ConfigurationResponseDto(Configuration configuration0) {
        this.id = configuration0.getId();
        this.name = configuration0.getName();
        this.value = configuration0.getValue();
    }
}
