# Users Management

This application exposes API endpoints to manage users (Create, Read, Update, Delete).

Every user has one or multiple roles (OWNER, OPERATOR, MAINTAINER, DEVELOPER, REPORTER) 

The project main class is **com.intesigroup.users.UsersApplication**

The application exposes a Swagger endpoint (http://localhost:8080/swagger-ui/index.html) that lets the user try 
the API endpoints.

It is also possible to access the in-memory H2 database using a web interface (http://localhost:8080/h2-console/login.jsp) 
using the following connection parameters:
- JDBC URL: jdbc:h2:mem:test
- Username: sa
- Password: [empty]




