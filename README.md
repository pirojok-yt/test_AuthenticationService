# Test authentication card service

Authentication service is a Spring Boot application to manage user registration and authentication with roles.
Expose APIs for user registration, authenticating registered users, check last login attempts, and manage cards with encrypted passwords and card numbers.

---

### Technologies / Changes

* Java 17
* Spring Boot 3.3.4
* Gradle
* Use docker-compose
* JDBC Client
* Swagger API documentation
* Spring security
* Liquibase
* Docker
* JWT
* PostgreSQL

---

### How to Build

- Navigate to the root directory and run: `./gradlew build`

### How to Run

  run 'run.sh' 

### API Documentation

Swagger: Access the Swagger API documentation
at: [http://localhost:8090/swagger-ui/index.html] 
when the application is running.

---

### Design Decisions

**Backend Service**
I have used Spring Boot 3.3.4(Spring MVC), which was released most recently

Our Tomcat will use virtual threads for HTTP requests, means our application runs on virtual threads to achieve high throughput.

**JDBC Client** is used, since Spring Framework 6.1 introduced JDBC Client that gives us a fluent API for talking to a database.

**API Clients**
* Swagger is used to call the endpoints.
* Postman was used to test endpoints

**Service(Client) to service(backend) communication**

* Spring MVC app(Servlet stack)
    * Rest Client(Fluent API)
