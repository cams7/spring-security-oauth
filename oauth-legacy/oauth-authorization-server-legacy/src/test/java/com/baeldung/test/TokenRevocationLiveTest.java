package com.baeldung.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;

// need both oauth-authorization-server-legacy and oauth-resource-server-legacy-1 to be running

public class TokenRevocationLiveTest {

  @Test
  public void whenObtainingAccessToken_thenCorrect() {
    final Response authServerResponse = obtainAccessToken("fooClientIdPassword", "john", "123");
    final String accessToken = authServerResponse.jsonPath().getString("access_token");
    assertNotNull(accessToken);

    final Response resourceServerResponse = RestAssured.given().header("Authorization", "Bearer " + accessToken)
        .get("http://localhost:8082/spring-security-oauth-resource/foos/100");
    assertEquals(200, resourceServerResponse.getStatusCode());
  }

  //

  private Response obtainAccessToken(String clientId, String username, String password) {
    final Map<String, String> params = new HashMap<String, String>();
    params.put("grant_type", "password");
    params.put("client_id", clientId);
    params.put("username", username);
    params.put("password", password);
    return RestAssured.given().auth().preemptive().basic(clientId, "secret").and().with().params(params).when()
        .post("http://localhost:8081/spring-security-oauth-server/oauth/token");
    // response.jsonPath().getString("refresh_token");
    // response.jsonPath().getString("access_token")
  }

  private String obtainRefreshToken(String clientId, final String refreshToken) {
    final Map<String, String> params = new HashMap<String, String>();
    params.put("grant_type", "refresh_token");
    params.put("client_id", clientId);
    params.put("refresh_token", refreshToken);
    final Response response = RestAssured.given().auth().preemptive().basic(clientId, "secret").and().with()
        .params(params).when().post("http://localhost:8081/spring-security-oauth-server/oauth/token");
    return response.jsonPath().getString("access_token");
  }

  private void authorizeClient(String clientId) {
    final Map<String, String> params = new HashMap<String, String>();
    params.put("response_type", "code");
    params.put("client_id", clientId);
    params.put("scope", "read,write");
    final Response response = RestAssured.given().auth().preemptive().basic(clientId, "secret").and().with()
        .params(params).when().post("http://localhost:8081/spring-security-oauth-server/oauth/authorize");
  }
}
