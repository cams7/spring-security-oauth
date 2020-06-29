package com.baeldung.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import com.baeldung.AuthorizationServerApplication;

@SpringBootTest(classes = AuthorizationServerApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthServerIntegrationTest {

  @Test
  public void whenLoadApplication_thenSuccess() {

  }
}
