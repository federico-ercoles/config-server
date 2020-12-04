package com.ercoles.configserver.services;

import com.ercoles.configserver.dtos.ConfigurationResponseDto;
import com.ercoles.configserver.repositories.ConfigCache;
import com.ercoles.configserver.repositories.ConfigCacheException;
import com.ercoles.configserver.repositories.ConfigCacheFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigService {
    private final ConfigCache configCache;

    public ConfigService(@Autowired ConfigCacheFactory factory) {
        this.configCache = factory.getConfigCache();
    }

    public List<ConfigurationResponseDto> getAll() throws ConfigServiceException {
        try {
            return configCache.getAll();
        } catch (ConfigCacheException e) {
            throw new ConfigServiceException("Error retrieving configuration list.", e);
        }
    }

    public ConfigurationResponseDto getConfiguration(String configId) throws ConfigServiceException {
        try {
            return configCache.get(configId);
        } catch (ConfigCacheException e) {
            throw new ConfigServiceException("Error retrieving configuration.", e);
        }
    }

    public ConfigurationResponseDto createConfiguration(String configId, String configName, String configValue) throws ConfigServiceException {
        try {
            return configCache.create(configId, configName, configValue);
        } catch (ConfigCacheException e) {
            throw new ConfigServiceException("Error creating new configuration.", e);
        }
    }

    public ConfigurationResponseDto updateConfiguration(String configId, String configName, String configValue) throws ConfigServiceException {
        try {
            return configCache.update(configId, configName, configValue);
        } catch (ConfigCacheException e) {
            throw new ConfigServiceException("Error updating configuration.", e);
        }
    }

    public ConfigurationResponseDto deleteConfiguration(String configId) throws ConfigServiceException {
        try {
            return configCache.delete(configId);
        } catch (ConfigCacheException e) {
            throw new ConfigServiceException("Error deleting configuration.", e);
        }
    }
}
