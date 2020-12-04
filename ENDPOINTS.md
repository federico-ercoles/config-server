# ENDPOINTS
## [GET] /
Retrieves the list of all configurations currently stored in the Config Server.

Returns:
* Http status 200 on success, with the list of stored configurations in the response body.
* Http status 500 on generic error, with an error message in the response body.
### Parameters
None.
### Sample request
```
curl --location --request GET 'http://localhost:8080/'
```
### Sample response
```json
[
    {
        "id": "test2",
        "name": "Test Configuration 2",
        "value": "test-2"
    },
    {
        "id": "test1",
        "name": "Test Configuration 1",
        "value": "test-1"
    }
]
```
#### Response definitions
Response item | Description | Data type
--------------|-------------|----------
__id__|A unique identifier of the configuration.|String
__name__|A human-readable name or description of the configuration.|String
__value__|The value of the configuration.|String
### Sample error response
```json
{
  "error": "Error retrieving configuration list. Caused by: Unable to get configuration list."
}
```

## [POST] /{id}
Creates a new configuration with the ID provided in the URL and the data provided in the request body.

Returns:
* Http status 201 on success, with the newly created configuration in the response body.
* Http status 400 on invalid data (empty strings for _name_ or _value_ attributes), with an error message in the response body.
* Http status 409 on an invalid id (already in use), with an error message in the response body.
* Http status 500 on generic error, with an error message in the response body.
### Parameters
#### Path parameters
Path parameter|Description
--------------|-----------
__id__|The identifier for the configuration you want to create.
### Sample request
```
curl --location --request POST 'http://localhost:8080/test1' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Test Configuration 1",
    "value": "test-1"
}'
```
### Sample response
```json
{
    "id": "test1",
    "name": "Test Configuration 1",
    "value": "test-1"
}
```
#### Response definitions
Response item | Description | Data type
--------------|-------------|----------
__id__|A unique identifier of the configuration.|String
__name__|A human-readable name or description of the configuration.|String
__value__|The value of the configuration.|String
### Sample error response
```json
{
  "error": "Submitted configuration parameters are not valid."
}
```

## [GET] /{id}
Retrieves the configuration data for the specified id.

Returns:
* Http status 200 on success, with the requested configuration in the response body.
* Http status 404 on configuration not found, with an error message in the response body.
### Parameters
#### Path parameters
Path parameter|Description
--------------|-----------
__id__|The identifier for the configuration you want to retrieve.
### Sample request
```
curl --location --request GET 'http://localhost:8080/test1'
```
### Sample response
```json
{
    "id": "test1",
    "name": "Test Configuration 1",
    "value": "test-1"
}
```
#### Response definitions
Response item | Description | Data type
--------------|-------------|----------
__id__|A unique identifier of the configuration.|String
__name__|A human-readable name or description of the configuration.|String
__value__|The value of the configuration.|String
### Sample error response
```json
{
  "error": "Error retrieving configuration. Caused by: No existing configuration for ID testX."
}
```

## [PUT] /{id}
Updates the configuration data for the specified id with the new information provided in the request body.

Returns:
* Http status 200 on success, with the updated configuration in the response body.
* Http status 400 on invalid data (empty strings for _name_ or _value_ attributes), with an error message in the response body.
* Http status 404 on configuration not found, with an error message in the response body.
* Http status 500 on generic error, with an error message in the response body.
### Parameters
#### Path parameters
Path parameter|Description
--------------|-----------
__id__|The identifier for the configuration you want to update.
### Sample request
```
curl --location --request PUT 'http://localhost:8080/test1' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Configurazione Test 1",
    "value": "test-1-a"
}'
```
### Sample response
```json
{
    "id": "test1",
    "name": "Configurazione Test 1",
    "value": "test-1-a"
}
```
#### Response definitions
Response item | Description | Data type
--------------|-------------|----------
__id__|A unique identifier of the configuration.|String
__name__|A human-readable name or description of the configuration.|String
__value__|The value of the configuration.|String
### Sample error response
```json
{
  "error": "Error updating configuration. Caused by: No existing configuration for ID testX."
}
```

## [DELETE] /{id}
Deletes the configuration data for the specified id.

Returns:
* Http status 200 on success, with the deleted configuration in the response body.
* Http status 404 on configuration not found, with an error message in the response body.
* Http status 500 on generic error, with an error message in the response body.
### Parameters
#### Path parameters
Path parameter|Description
--------------|-----------
__id__|The identifier for the configuration you want to delete.
### Sample request
```
curl --location --request DELETE 'http://localhost:8080/test1'
```
### Sample response
```json
{
    "id": "test1",
    "name": "Configurazione Test 1",
    "value": "test-1"
}
```
#### Response definitions
Response item | Description | Data type
--------------|-------------|----------
__id__|A unique identifier of the configuration.|String
__name__|A human-readable name or description of the configuration.|String
__value__|The value of the configuration.|String
### Sample error response
```json
{
  "error": "Error deleting configuration. Caused by: No existing configuration for ID testX."
}
```
