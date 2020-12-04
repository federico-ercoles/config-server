package com.ercoles.configserver.repositories;

import org.springframework.stereotype.Component;

@Component
public class ConfigCacheFactory {
    private ConfigCache configCache;

    public ConfigCache getConfigCache() {
        if (configCache == null) {
            // can be extended to check for properties specific for a database, or Redis, or filesystem
            // and instantiate the corresponding implementation
            configCache = new ConfigCacheMemory();
        }
        return configCache;
    }
}
