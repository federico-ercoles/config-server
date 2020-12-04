# Config Server
Config Server is a basic application that implements the configuration server functionality needed to manage a micro-service infrastructure.

## Installation and Start
Clone the repository in your local environment. Navigate to the newly created folder and execute the following command:

`mvn clean package`

This will create a JAR file inside the _target_ folder. Start the application with the command:

`java -jar target/config-server-DEMO.jar`

## How to use
For a detailed description of each API, please refer to the ENDPOINTS.md file.

## How to contribute
You can fork the repository on your personal account. 
The project has a suite of unit and integration tests to validate its basic functionality.
You can run them to ensure your changes did not break anything by executing the following command from the main folder:

`mvn test`

When your changes are ready, you can submit a pull request that will get validated as soon as possible.
If your contribution is solid, it will get merged in the main repository.

## Areas of improvements

#### - Add more options to store configurations.
At the moment, configurations are stored in memory, which means they are deleted when the application stops.
This can be expanded by adding the option to store them in a database or in Redis, or write them in a file.

#### - Add the option to group configurations by service
At the moment, all configurations are at the same level.
This can be improved by separating configurations by the service to which they are relevant.
Furthermore, a new API to get the list of configurations specific for a service could be implemented.

#### - Add an authorization system
At the moment, all endpoints are callable without authentication or any permission check.
This can be improved by implementing an authentication/authorization provider (or integrating with one), in order to restrict access to certain actions (like update or delete), or to a certain service configuration list. 