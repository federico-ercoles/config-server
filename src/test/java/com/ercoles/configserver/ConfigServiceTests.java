package com.ercoles.configserver;

import com.ercoles.configserver.dtos.ConfigurationResponseDto;
import com.ercoles.configserver.repositories.ConfigCacheException;
import com.ercoles.configserver.services.ConfigService;
import com.ercoles.configserver.services.ConfigServiceException;
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
public class ConfigServiceTests {
    private final ConfigService configService;

    public ConfigServiceTests(@Autowired ConfigService configService) {
        this.configService = configService;
    }

    @Test
    @Order(1)
    void testConfigService_emptyAtStart() throws ConfigServiceException {
        // GIVEN configService
        // WHEN
        List<ConfigurationResponseDto> configurations = configService.getAll();
        // THEN
        assertThat(configurations).isEmpty();
    }

    @Test
    @Order(2)
    void testConfigService_createConfigurationSuccess() throws ConfigServiceException {
        // GIVEN
        String configId1 = "test1";
        String configName1 = "Test Configuration 1";
        String configValue1 = "test-1";
        // WHEN
        ConfigurationResponseDto testConfiguration1 = configService.createConfiguration(configId1, configName1, configValue1);
        List<ConfigurationResponseDto> configurations = configService.getAll();
        // THEN
        assertThat(configurations).hasSize(1);
        assertThat(configurations).contains(testConfiguration1);
        assertThat(configurations).containsExactly(testConfiguration1);

        // GIVEN
        String configId2 = "test2";
        String configName2 = "Test Configuration 2";
        String configValue2 = "test-2";
        // WHEN
        ConfigurationResponseDto testConfiguration2 = configService.createConfiguration(configId2, configName2, configValue2);
        configurations = configService.getAll();
        // THEN
        assertThat(configurations).hasSize(2);
        assertThat(configurations).contains(testConfiguration1);
        assertThat(configurations).contains(testConfiguration2);
        assertThat(configurations).containsExactlyInAnyOrder(testConfiguration1, testConfiguration2);
    }

    @Test
    @Order(3)
    void testConfigService_createConfigurationException_invalidParameters() {
        // GIVEN
        String configId = "test-exception";
        String configName = "Test Exception";
        String configValue = "test-exception";
        // WHEN
        Throwable thrown1 = catchThrowable(() -> configService.createConfiguration(null, configName, configValue));
        Throwable thrown2 = catchThrowable(() -> configService.createConfiguration(configId, null, configValue));
        Throwable thrown3 = catchThrowable(() -> configService.createConfiguration(configId, configName, null));
        // THEN
        assertThat(thrown1).isInstanceOf(NullPointerException.class);
        assertThat(thrown2).isInstanceOf(NullPointerException.class);
        assertThat(thrown3).isInstanceOf(NullPointerException.class);
    }

    @Test
    @Order(3)
    void testConfigService_createConfigurationException_configurationAlreadyExists() {
        // GIVEN
        String configId = "test1";
        String configName = "Test Exception";
        String configValue = "test-exception";
        // WHEN
        Throwable thrown = catchThrowable(() -> configService.createConfiguration(configId, configName, configValue));
        // THEN
        assertThat(thrown).isInstanceOf(ConfigServiceException.class)
                .hasCauseInstanceOf(ConfigCacheException.class)
                .hasMessage("Error creating new configuration.");
    }

    @Test
    @Order(4)
    void testConfigService_retrieveConfigurationSuccess() throws ConfigServiceException {
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
        ConfigurationResponseDto storedConfiguration1 = configService.getConfiguration(configId1);
        ConfigurationResponseDto storedConfiguration2 = configService.getConfiguration(configId2);
        // THEN
        assertThat(storedConfiguration1).isInstanceOf(ConfigurationResponseDto.class);
        assertThat(storedConfiguration1).isEqualTo(testConfiguration1);
        assertThat(storedConfiguration2).isInstanceOf(ConfigurationResponseDto.class);
        assertThat(storedConfiguration2).isEqualTo(testConfiguration2);
    }

    @Test
    @Order(5)
    void testConfigService_retrieveConfigurationException() {
        // GIVEN
        String configId = "test-exception";
        // WHEN
        Throwable thrown = catchThrowable(() -> configService.getConfiguration(configId));
        // THEN
        assertThat(thrown).isInstanceOf(ConfigServiceException.class)
                .hasCauseInstanceOf(ConfigCacheException.class)
                .hasMessage("Error retrieving configuration.");
    }

    @Test
    @Order(6)
    void testConfigService_updateConfigurationSuccess() throws ConfigServiceException {
        // GIVEN
        String configId1 = "test1";
        String configName1 = "Test Configuration 1";
        String configValue1Updated = "test-1a";
        // WHEN
        ConfigurationResponseDto testConfigurationUpdated = configService.updateConfiguration(configId1, configName1, configValue1Updated);
        // THEN
        assertThat(testConfigurationUpdated.getValue()).isEqualTo(configValue1Updated);
    }

    @Test
    @Order(7)
    void testConfigService_updateConfigurationException() {
        // GIVEN
        String configId = "test-exception";
        String configName = "Test Exception";
        String configValue = "test-1ex";
        // WHEN
        Throwable thrown = catchThrowable(() -> configService.updateConfiguration(configId, configName, configValue));
        // THEN
        assertThat(thrown).isInstanceOf(ConfigServiceException.class)
                .hasCauseInstanceOf(ConfigCacheException.class)
                .hasMessage("Error updating configuration.");
    }

    @Test
    @Order(8)
    void testConfigService_retrieveAll() throws ConfigServiceException {
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
        List<ConfigurationResponseDto> configurations = configService.getAll();
        // THEN
        assertThat(configurations).hasSize(2);
        assertThat(configurations).contains(testConfiguration1);
        assertThat(configurations).contains(testConfiguration2);
        assertThat(configurations).containsExactlyInAnyOrder(testConfiguration1, testConfiguration2);
    }

    @Test
    @Order(9)
    void testConfigService_deleteConfigurationSuccess() throws ConfigServiceException {
        // GIVEN
        String configId2 = "test2";
        // WHEN
        ConfigurationResponseDto deletedConfiguration2 = configService.deleteConfiguration(configId2);
        List<ConfigurationResponseDto> configurations = configService.getAll();
        // THEN
        assertThat(configurations).doesNotContain(deletedConfiguration2);

        // GIVEN
        String configId1 = "test1";
        // WHEN
        ConfigurationResponseDto deletedConfiguration1 = configService.deleteConfiguration(configId1);
        configurations = configService.getAll();
        // THEN
        assertThat(configurations).doesNotContain(deletedConfiguration1);
    }

    @Test
    @Order(10)
    void testConfigService_deleteConfigurationException() {
        // GIVEN
        String configId = "test-exception";
        // WHEN
        Throwable thrown = catchThrowable(() -> configService.deleteConfiguration(configId));
        // THEN
        assertThat(thrown).isInstanceOf(ConfigServiceException.class)
                .hasCauseInstanceOf(ConfigCacheException.class)
                .hasMessage("Error deleting configuration.");
    }

    @Test
    @Order(11)
    void testConfigService_emptyAtEnd() throws ConfigServiceException {
        // GIVEN configService
        // WHEN
        List<ConfigurationResponseDto> configurations = configService.getAll();
        // THEN
        assertThat(configurations).isEmpty();
    }

}
