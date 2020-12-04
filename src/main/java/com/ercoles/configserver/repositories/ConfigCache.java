package com.ercoles.configserver.repositories;

import com.ercoles.configserver.dtos.ConfigurationResponseDto;

import java.util.List;

public interface ConfigCache {
    List<ConfigurationResponseDto> getAll() throws ConfigCacheException;
    ConfigurationResponseDto get(String id) throws ConfigCacheException;
    ConfigurationResponseDto create(String id, String name, String value) throws ConfigCacheException;
    ConfigurationResponseDto update(String id, String name, String value) throws ConfigCacheException;
    ConfigurationResponseDto delete(String id) throws ConfigCacheException;
}
