# Users Management

This application exposes API endpoints to manage users (Create, Read, Update, Delete).

Every user has one or multiple roles (OWNER, OPERATOR, MAINTAINER, DEVELOPER, REPORTER) 

The project main class is **com.intesigroup.users.UsersApplication**

The application needs to be executed using Docker Compose:
- mvn clean
- mvn package
- docker compose up
- If you need to deploy new changes to the application, remember to delete the application Docker image from the local repository before running docker compose again

The application exposes a Swagger endpoint (http://localhost:8080/swagger-ui/index.html) that lets the user try 
the API endpoints.

The APIs need an authorization token (JWT) to be called.

It is also possible to access the in-memory H2 database using a web interface (http://localhost:8080/h2-console/login.jsp) 
using the following connection parameters:
- JDBC URL: jdbc:h2:mem:test
- Username: sa
- Password: [empty]

It is possible to access a RabbitMQ Manager GUI on http://localhost:15672/, using the following parameters:
- Username: app
- Password: app_password_12345




