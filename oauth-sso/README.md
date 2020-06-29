## Spring Security OAuth - Dynamic Client Registration

### Relevant information:

1. `sso-authorization-server` is a Keycloak Authorization Server wrapped as a Spring Boot application
2. There is one OAuth Client registered in the Authorization Server:
   1. Client Id: ssoClient
   2. Client secret: ssoClientSecret
   3. Redirect Uri: http://localhost:8089/
3. `sso-resource-server` is a Spring Boot based RESTFul API, acting as a backend Application
4. `sso-client-app-1` and `sso-client-app-2` are two identical Spring MVC Thymeleaf App acting our front end. They are available at [http://localhost:8082/ui-one/](http://localhost:8082/ui-one) and [http://localhost:8084/ui-two/](http://localhost:8084/ui-two/) respectively.
5. There are two users registered in the Authorization Server:
   1. john@test.com / 123
   2. mike@other.com / pass

## Relevant Articles: 

