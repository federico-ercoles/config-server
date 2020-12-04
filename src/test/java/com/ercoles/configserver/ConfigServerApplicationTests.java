package com.ercoles.configserver;

import com.ercoles.configserver.controllers.Controller;
import com.ercoles.configserver.repositories.ConfigCache;
import com.ercoles.configserver.repositories.ConfigCacheFactory;
import com.ercoles.configserver.services.ConfigService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ConfigServerApplicationTests {
    private final Controller controller;
    private final ConfigService configService;
    private final ConfigCacheFactory configCacheFactory;
    private final ConfigCache configCache;

    public ConfigServerApplicationTests(
            @Autowired Controller controller,
            @Autowired ConfigService configService,
            @Autowired ConfigCacheFactory configCacheFactory
    ) {
        this.controller = controller;
        this.configService = configService;
        this.configCacheFactory = configCacheFactory;
        this.configCache = configCacheFactory.getConfigCache();
    }

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
        assertThat(controller).isInstanceOf(Controller.class);
        assertThat(configService).isNotNull();
        assertThat(configService).isInstanceOf(ConfigService.class);
        assertThat(configCacheFactory).isNotNull();
        assertThat(configCacheFactory).isInstanceOf(ConfigCacheFactory.class);
        assertThat(configCache).isNotNull();
        assertThat(configCache).isInstanceOf(ConfigCache.class);
    }

}
