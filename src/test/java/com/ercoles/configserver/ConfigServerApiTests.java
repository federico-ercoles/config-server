package com.ercoles.configserver;

import com.ercoles.configserver.dtos.ConfigurationResponseDto;
import com.ercoles.configserver.dtos.ErrorDto;
import com.ercoles.configserver.repositories.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ConfigServerApiTests {

    @LocalServerPort
    private int port;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ConfigServerApiTests() {
        this.httpClient = HttpClientBuilder.create().build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void testApi_emptyAtStart() throws IOException {
        // GIVEN
        String url = "http://localhost:" + port + "/";
        HttpGet request = new HttpGet(url);
        // WHEN
        HttpResponse response = httpClient.execute(request);
        String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        List<Configuration> resources = objectMapper.readerForListOf(Configuration.class).readValue(response.getEntity().getContent());
        // THEN
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(mimeType).isEqualTo("application/json");
        assertThat(resources).isEmpty();
    }

    @Test
    @Order(2)
    void testApi_createConfigurationSuccess() throws JSONException, IOException {
        // GIVEN
        String configId1 = "test1";
        String configName1 = "Test Configuration 1";
        String configValue1 = "test-1";
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("name", configName1);
        jsonObject1.put("value", configValue1);
        String url1 = "http://localhost:" + port + "/" + configId1;
        HttpPost request1 = new HttpPost(url1);
        request1.addHeader("content-type", "application/json");
        request1.setEntity(new StringEntity(jsonObject1.toString()));
        // WHEN
        HttpResponse response1 = httpClient.execute(request1);
        String mimeType1 = ContentType.getOrDefault(response1.getEntity()).getMimeType();
        ConfigurationResponseDto resource1 = objectMapper.readValue(response1.getEntity().getContent(), ConfigurationResponseDto.class);
        // THEN
        assertThat(response1.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(mimeType1).isEqualTo("application/json");
        assertThat(resource1.getId()).isEqualTo(configId1);
        assertThat(resource1.getName()).isEqualTo(configName1);
        assertThat(resource1.getValue()).isEqualTo(configValue1);

        // GIVEN
        String configId2 = "test2";
        String configName2 = "Test Configuration 2";
        String configValue2 = "test-2";
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("name", configName2);
        jsonObject2.put("value", configValue2);
        String url2 = "http://localhost:" + port + "/" + configId2;
        HttpPost request2 = new HttpPost(url2);
        request2.addHeader("content-type", "application/json");
        request2.setEntity(new StringEntity(jsonObject2.toString()));
        // WHEN
        HttpResponse response2 = httpClient.execute(request2);
        String mimeType2 = ContentType.getOrDefault(response2.getEntity()).getMimeType();
        ConfigurationResponseDto resource2 = objectMapper.readValue(response2.getEntity().getContent(), ConfigurationResponseDto.class);
        // THEN
        assertThat(response2.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(mimeType2).isEqualTo("application/json");
        assertThat(resource2.getId()).isEqualTo(configId2);
        assertThat(resource2.getName()).isEqualTo(configName2);
        assertThat(resource2.getValue()).isEqualTo(configValue2);
    }

    @Test
    @Order(3)
    void testApi_createConfigurationError_invalidParameters() throws IOException, JSONException {
        // GIVEN
        String configId = "test-exception";
        String configName = "Test Exception";
        String configValue = "test-exception";
        String url = "http://localhost:" + port + "/" + configId;

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("name", "");
        jsonObject1.put("value", configValue);
        HttpPost request1 = new HttpPost(url);
        request1.addHeader("content-type", "application/json");
        request1.setEntity(new StringEntity(jsonObject1.toString()));
        // WHEN
        HttpResponse response1 = httpClient.execute(request1);
        String mimeType1 = ContentType.getOrDefault(response1.getEntity()).getMimeType();
        ErrorDto resource1 = objectMapper.readValue(response1.getEntity().getContent(), ErrorDto.class);
        // THEN
        assertThat(response1.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(mimeType1).isEqualTo("application/json");
        assertThat(resource1.getError()).isEqualTo("Submitted configuration parameters are not valid.");

        // GIVEN
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("name", configName);
        jsonObject2.put("value", "");
        HttpPost request2 = new HttpPost(url);
        request2.addHeader("content-type", "application/json");
        request2.setEntity(new StringEntity(jsonObject2.toString()));
        // WHEN
        HttpResponse response2 = httpClient.execute(request2);
        String mimeType2 = ContentType.getOrDefault(response2.getEntity()).getMimeType();
        ErrorDto resource2 = objectMapper.readValue(response2.getEntity().getContent(), ErrorDto.class);
        // THEN
        assertThat(response2.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(mimeType2).isEqualTo("application/json");
        assertThat(resource2.getError()).isEqualTo("Submitted configuration parameters are not valid.");
    }

    @Test
    @Order(3)
    void testApi_createConfigurationError_configurationAlreadyExists() throws IOException, JSONException {
        // GIVEN
        String configId = "test1";
        String configName = "Test Exception";
        String configValue = "test-exception";
        String url = "http://localhost:" + port + "/" + configId;

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("name", configName);
        jsonObject1.put("value", configValue);
        HttpPost request = new HttpPost(url);
        request.addHeader("content-type", "application/json");
        request.setEntity(new StringEntity(jsonObject1.toString()));
        // WHEN
        HttpResponse response = httpClient.execute(request);
        String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        ErrorDto resource = objectMapper.readValue(response.getEntity().getContent(), ErrorDto.class);
        // THEN
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(mimeType).isEqualTo("application/json");
        assertThat(resource.getError()).isEqualTo("Error creating new configuration. Caused by: ID test1 is already in use.");
    }

    @Test
    @Order(4)
    void testApi_retrieveConfigurationSuccess() throws IOException {
        // GIVEN
        String id = "test1";
        String url = "http://localhost:" + port + "/" + id;
        HttpGet request = new HttpGet(url);
        // WHEN
        HttpResponse response = httpClient.execute(request);
        String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        ConfigurationResponseDto resource = objectMapper.readValue(response.getEntity().getContent(), ConfigurationResponseDto.class);
        // THEN
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(mimeType).isEqualTo("application/json");
        assertThat(resource.getId()).isEqualTo(id);
        assertThat(resource.getName()).isEqualTo("Test Configuration 1");
        assertThat(resource.getValue()).isEqualTo("test-1");
    }

    @Test
    @Order(5)
    void testApi_retrieveConfigurationError() throws IOException {
        // GIVEN
        String id = "test-exception";
        String url = "http://localhost:" + port + "/" + id;
        HttpGet request = new HttpGet(url);
        // WHEN
        HttpResponse response = httpClient.execute(request);
        String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        ErrorDto resource = objectMapper.readValue(response.getEntity().getContent(), ErrorDto.class);
        // THEN
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(mimeType).isEqualTo("application/json");
        assertThat(resource.getError()).contains("No existing configuration");
    }

    @Test
    @Order(6)
    void testApi_updateConfigurationSuccess() throws JSONException, IOException {
        // GIVEN
        String configId = "test1";
        String configName = "Test Configuration 1";
        String configValueUpdated = "test-10";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", configName);
        jsonObject.put("value", configValueUpdated);
        String url = "http://localhost:" + port + "/" + configId;
        HttpPut request = new HttpPut(url);
        request.addHeader("content-type", "application/json");
        request.setEntity(new StringEntity(jsonObject.toString()));
        // WHEN
        HttpResponse response = httpClient.execute(request);
        String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        ConfigurationResponseDto resource = objectMapper.readValue(response.getEntity().getContent(), ConfigurationResponseDto.class);
        // THEN
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(mimeType).isEqualTo("application/json");
        assertThat(resource.getId()).isEqualTo(configId);
        assertThat(resource.getName()).isEqualTo(configName);
        assertThat(resource.getValue()).isEqualTo(configValueUpdated);
    }

    @Test
    @Order(7)
    void testApi_updateConfigurationError() throws IOException, JSONException {
        // GIVEN
        String configId = "test-exception";
        String configName = "Test Exception";
        String configValueUpdated = "test-exception";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", configName);
        jsonObject.put("value", configValueUpdated);
        String url = "http://localhost:" + port + "/" + configId;
        HttpPut request = new HttpPut(url);
        request.addHeader("content-type", "application/json");
        request.setEntity(new StringEntity(jsonObject.toString()));
        // WHEN
        HttpResponse response = httpClient.execute(request);
        String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        ErrorDto resource = objectMapper.readValue(response.getEntity().getContent(), ErrorDto.class);
        // THEN
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(mimeType).isEqualTo("application/json");
        assertThat(resource.getError()).contains("No existing configuration");
    }

    @Test
    @Order(8)
    void testApi_retrieveAll() throws IOException {
        // GIVEN
        String configId1 = "test1";
        String configName1 = "Test Configuration 1";
        String configValue1 = "test-10";
        String configId2 = "test2";
        String configName2 = "Test Configuration 2";
        String configValue2 = "test-2";
        ConfigurationResponseDto testConfiguration1 = new ConfigurationResponseDto(configId1, configName1, configValue1);
        ConfigurationResponseDto testConfiguration2 = new ConfigurationResponseDto(configId2, configName2, configValue2);
        HttpUriRequest request = new HttpGet("http://localhost:" + port + "/");
        // WHEN
        HttpResponse response = httpClient.execute(request);
        String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        List<ConfigurationResponseDto> resources = objectMapper.readerForListOf(ConfigurationResponseDto.class).readValue(response.getEntity().getContent());
        // THEN
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(mimeType).isEqualTo("application/json");
        assertThat(resources).contains(testConfiguration1);
        assertThat(resources).contains(testConfiguration2);
        assertThat(resources).containsExactlyInAnyOrder(testConfiguration1, testConfiguration2);
    }

    @Test
    @Order(9)
    void testApi_deleteConfigurationSuccess() throws IOException {
        // GIVEN
        String configId2 = "test2";
        String url2 = "http://localhost:" + port + "/" + configId2;
        HttpDelete request2 = new HttpDelete(url2);
        // WHEN
        HttpResponse response2 = httpClient.execute(request2);
        // THEN
        assertThat(response2.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.OK.value());

        // GIVEN
        String configId1 = "test1";
        String url1 = "http://localhost:" + port + "/" + configId1;
        HttpDelete request1 = new HttpDelete(url1);
        // WHEN
        HttpResponse response1 = httpClient.execute(request1);
        // THEN
        assertThat(response1.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @Order(10)
    void testApi_deleteConfigurationError() throws IOException {
        // GIVEN
        String configId = "test-exception";
        String url = "http://localhost:" + port + "/" + configId;
        HttpDelete request = new HttpDelete(url);
        // WHEN
        HttpResponse response = httpClient.execute(request);
        String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        ErrorDto resource = objectMapper.readValue(response.getEntity().getContent(), ErrorDto.class);
        // THEN
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(mimeType).isEqualTo("application/json");
        assertThat(resource.getError()).contains("No existing configuration");
    }

    @Test
    @Order(11)
    void testApi_emptyAtEnd() throws IOException {
        // GIVEN
        String url = "http://localhost:" + port + "/";
        HttpGet request = new HttpGet(url);
        // WHEN
        HttpResponse response = httpClient.execute(request);
        String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        List<Configuration> resources = objectMapper.readerForListOf(Configuration.class).readValue(response.getEntity().getContent());
        // THEN
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(mimeType).isEqualTo("application/json");
        assertThat(resources).isEmpty();
    }
}