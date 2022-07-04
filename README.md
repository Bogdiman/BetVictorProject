To build and run the application, simply run the following command:
mvn clean build spring-boot:run

Open a browser window on http://localhost:8080

For health check, you can use http://localhost:8080/actuator/health
To display the version of the program, use http://localhost:8080/actuator/info

For testing purposes, no mocks were used, as the application is quite simple.