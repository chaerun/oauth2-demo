package com.chaerun.demo.oauth2;

import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(properties = {
    "keycloak.admin.server-url=http://localhost:8080",
    "keycloak.admin.realm=master",
    "keycloak.admin.target-realm=master",
    "keycloak.admin.client-id=client",
    "keycloak.admin.client-secret=secret",
    // Prevent auto-config from contacting the OIDC issuer during test
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientAutoConfiguration,org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerAutoConfiguration"
})
class Oauth2DemoApplicationTests {

  @MockitoBean
  private Keycloak keycloakAdminClient; // replace existing bean in KeycloakAdminConfig

  @MockitoBean
  private ClientRegistrationRepository clientRegistrationRepository; // satisfy SecurityConfig

  @MockitoBean
  private JwtDecoder jwtDecoder; // satisfy oauth2 resource server expectation

  @Test
  void contextLoads() {
  }

}
