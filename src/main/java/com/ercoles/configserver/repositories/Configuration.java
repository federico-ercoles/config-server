package com.ercoles.configserver.repositories;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Configuration {
    @NonNull
    String id;
    @NonNull
    String name;
    @NonNull
    String value;
}
