package com.chaerun.demo.oauth2.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

class ApiControllerTest {

  @Test
  void getApiData_returnsExpectedMap() {
    // Arrange: build a Jwt with the expected claim and issuer
    Instant now = Instant.now();
    Jwt jwt = Jwt.withTokenValue("token-value")
        .header("alg", "none")
        .claim("azp", "demo-client")
        .issuer("http://issuer.example.com")
        .issuedAt(now)
        .expiresAt(now.plusSeconds(3600))
        .build();

    ApiController controller = new ApiController();

    // Act
    Map<String, Object> result = controller.getApiData(jwt);

    // Assert
    assertEquals("Hello from a protected API", result.get("message"));
    assertEquals("demo-client", result.get("clientId"));
    assertEquals("http://issuer.example.com", result.get("issuer"));
  }

}
