package com.ercoles.configserver;

import com.ercoles.configserver.dtos.ConfigurationResponseDto;
import com.ercoles.configserver.repositories.ConfigCache;
import com.ercoles.configserver.repositories.ConfigCacheException;
import com.ercoles.configserver.repositories.ConfigCacheFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ConfigCacheTests {
    private final ConfigCache configCache;

    public ConfigCacheTests(@Autowired ConfigCacheFactory factory) {
        this.configCache = factory.getConfigCache();
    }

    @Test
    @Order(1)
    void testConfigCache_emptyAtStart() throws ConfigCacheException {
        // GIVEN configCache
        // WHEN
        List<ConfigurationResponseDto> configurations = configCache.getAll();
        // THEN
        assertThat(configurations).isEmpty();
    }

    @Test
    @Order(2)
    void testConfigCache_createConfigurationSuccess() throws ConfigCacheException {
        // GIVEN
        String configId1 = "test1";
        String configName1 = "Test Configuration 1";
        String configValue1 = "test-1";
        // WHEN
        ConfigurationResponseDto testConfiguration1 = configCache.create(configId1, configName1, configValue1);
        List<ConfigurationResponseDto> configurations = configCache.getAll();
        // THEN
        assertThat(testConfiguration1.getId()).isEqualTo(configId1);
        assertThat(testConfiguration1.getName()).isEqualTo(configName1);
        assertThat(testConfiguration1.getValue()).isEqualTo(configValue1);
        assertThat(configurations).hasSize(1);
        assertThat(configurations).contains(testConfiguration1);
        assertThat(configurations).containsExactly(testConfiguration1);

        // GIVEN
        String configId2 = "test2";
        String configName2 = "Test Configuration 2";
        String configValue2 = "test-2";
        // WHEN
        ConfigurationResponseDto testConfiguration2 = configCache.create(configId2, configName2, configValue2);
        configurations = configCache.getAll();
        // THEN
        assertThat(testConfiguration2.getId()).isEqualTo(configId2);
        assertThat(testConfiguration2.getName()).isEqualTo(configName2);
        assertThat(testConfiguration2.getValue()).isEqualTo(configValue2);
        assertThat(configurations).hasSize(2);
        assertThat(configurations).contains(testConfiguration1);
        assertThat(configurations).contains(testConfiguration2);
        assertThat(configurations).containsExactlyInAnyOrder(testConfiguration1, testConfiguration2);
    }

    @Test
    @Order(3)
    void testConfigCache_createConfigurationException_invalidParameters() {
        // GIVEN
        String configId = "test-exception";
        String configName = "Test Exception";
        String configValue = "test-exception";
        // WHEN
        Throwable thrown1 = catchThrowable(() -> configCache.create(null, configName, configValue));
        Throwable thrown2 = catchThrowable(() -> configCache.create(configId, null, configValue));
        Throwable thrown3 = catchThrowable(() -> configCache.create(configId, configName, null));
        // THEN
        assertThat(thrown1).isInstanceOf(NullPointerException.class);
        assertThat(thrown2).isInstanceOf(NullPointerException.class);
        assertThat(thrown3).isInstanceOf(NullPointerException.class);
    }

    @Test
    @Order(3)
    void testConfigCache_createConfigurationException_configurationAlreadyExists() {
        // GIVEN
        String configId = "test1";
        String configName = "Test Exception";
        String configValue = "test-exception";
        // WHEN
        Throwable thrown = catchThrowable(() -> configCache.create(configId, configName, configValue));
        // THEN
        assertThat(thrown).isInstanceOf(ConfigCacheException.class).hasMessageContaining("already in use");
    }

    @Test
    @Order(4)
    void testConfigCache_retrieveConfigurationSuccess() throws ConfigCacheException {
        // GIVEN
        String configId1 = "test1";
        String configName1 = "Test Configuration 1";
        String configValue1 = "test-1";
        ConfigurationResponseDto testConfiguration1 = new ConfigurationResponseDto(configId1, configName1, configValue1);
        String configId2 = "test2";
        String configName2 = "Test Configuration 2";
        String configValue2 = "test-2";
        ConfigurationResponseDto testConfiguration2 = new ConfigurationResponseDto(configId2, configName2, configValue2);
        // WHEN
        ConfigurationResponseDto storedConfiguration1 = configCache.get(configId1);
        ConfigurationResponseDto storedConfiguration2 = configCache.get(configId2);
        // THEN
        assertThat(storedConfiguration1).isInstanceOf(ConfigurationResponseDto.class);
        assertThat(storedConfiguration1).isEqualTo(testConfiguration1);
        assertThat(storedConfiguration2).isInstanceOf(ConfigurationResponseDto.class);
        assertThat(storedConfiguration2).isEqualTo(testConfiguration2);
    }

    @Test
    @Order(5)
    void testConfigCache_retrieveConfigurationException() {
        // GIVEN
        String configId = "test-exception";
        // WHEN
        Throwable thrown = catchThrowable(() -> configCache.get(configId));
        // THEN
        assertThat(thrown).isInstanceOf(ConfigCacheException.class).hasMessageContaining("No existing configuration");
    }

    @Test
    @Order(6)
    void testConfigCache_updateConfigurationSuccess() throws ConfigCacheException {
        // GIVEN
        String configId1 = "test1";
        String configName1 = "Test Configuration 1";
        String configValue1Updated = "test-1a";
        // WHEN
        ConfigurationResponseDto testConfigurationUpdated = configCache.update(configId1, configName1, configValue1Updated);
        // THEN
        assertThat(testConfigurationUpdated.getValue()).isEqualTo(configValue1Updated);
    }

    @Test
    @Order(7)
    void testConfigCache_updateConfigurationException() {
        // GIVEN
        String configId = "test-exception";
        String configName = "Test Exception";
        String configValue = "test-1ex";
        // WHEN
        Throwable thrown = catchThrowable(() -> configCache.update(configId, configName, configValue));
        // THEN
        assertThat(thrown).isInstanceOf(ConfigCacheException.class).hasMessageContaining("No existing configuration");
    }

    @Test
    @Order(8)
    void testConfigCache_retrieveAll() throws ConfigCacheException {
        // GIVEN
        String configId1 = "test1";
        String configName1 = "Test Configuration 1";
        String configValue1 = "test-1a";
        ConfigurationResponseDto testConfiguration1 = new ConfigurationResponseDto(configId1, configName1, configValue1);
        String configId2 = "test2";
        String configName2 = "Test Configuration 2";
        String configValue2 = "test-2";
        ConfigurationResponseDto testConfiguration2 = new ConfigurationResponseDto(configId2, configName2, configValue2);
        // WHEN
        List<ConfigurationResponseDto> configurations = configCache.getAll();
        // THEN
        assertThat(configurations).hasSize(2);
        assertThat(configurations).contains(testConfiguration1);
        assertThat(configurations).contains(testConfiguration2);
        assertThat(configurations).containsExactlyInAnyOrder(testConfiguration1, testConfiguration2);
    }

    @Test
    @Order(9)
    void testConfigCache_deleteConfigurationSuccess() throws ConfigCacheException {
        // GIVEN
        String configId2 = "test2";
        // WHEN
        ConfigurationResponseDto deletedConfiguration2 = configCache.delete(configId2);
        // THEN
        assertThat(configCache.getAll()).doesNotContain(deletedConfiguration2);

        // GIVEN
        String configId1 = "test1";
        // WHEN
        ConfigurationResponseDto deletedConfiguration1 = configCache.delete(configId1);
        // THEN
        assertThat(configCache.getAll()).doesNotContain(deletedConfiguration1);
    }

    @Test
    @Order(10)
    void testConfigCache_deleteConfigurationException() {
        // GIVEN
        String configId = "test-exception";
        // WHEN
        Throwable thrown = catchThrowable(() -> configCache.delete(configId));
        // THEN
        assertThat(thrown).isInstanceOf(ConfigCacheException.class).hasMessageContaining("No existing configuration");
    }

    @Test
    @Order(11)
    void testConfigCache_emptyAtEnd() throws ConfigCacheException {
        // GIVEN configCache
        // WHEN
        List<ConfigurationResponseDto> configurations = configCache.getAll();
        // THEN
        assertThat(configurations).isEmpty();
    }

}
