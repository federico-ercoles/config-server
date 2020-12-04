package com.ercoles.configserver.repositories;

import com.ercoles.configserver.dtos.ConfigurationResponseDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ConfigCacheMemory implements ConfigCache {
    private final Map<String, Configuration> cache = new HashMap<>();

    @Override
    public List<ConfigurationResponseDto> getAll() throws ConfigCacheException {
        try {
            return cache.values().stream().map(ConfigurationResponseDto::new).collect(Collectors.toList());
        } catch (Exception e) {
            throw new ConfigCacheException("Unable to get configuration list.");
        }
    }

    @Override
    public ConfigurationResponseDto get(String id) throws ConfigCacheException {
        if (!cache.containsKey(id)) {
            throw new ConfigCacheException("No existing configuration for ID " + id + ".");
        }
        return new ConfigurationResponseDto(cache.get(id));
    }

    @Override
    public ConfigurationResponseDto create(String id, String name, String value) throws ConfigCacheException {
        if (cache.containsKey(id)) {
            throw new ConfigCacheException("ID " + id + " is already in use.");
        }
        Configuration configuration = new Configuration(id, name, value);
        cache.put(id, configuration);
        return new ConfigurationResponseDto(configuration);
    }

    @Override
    public ConfigurationResponseDto update(String id, String name, String value) throws ConfigCacheException {
        if (!cache.containsKey(id)) {
            throw new ConfigCacheException("No existing configuration for ID " + id + ".");
        }
        Configuration configuration = new Configuration(id, name, value);
        cache.replace(id, configuration);
        return new ConfigurationResponseDto(configuration);
    }

    @Override
    public ConfigurationResponseDto delete(String id) throws ConfigCacheException {
        if (!cache.containsKey(id)) {
            throw new ConfigCacheException("No existing configuration for ID " + id + ".");
        }
        Configuration configuration = cache.remove(id);
        return new ConfigurationResponseDto(configuration);
    }
}
