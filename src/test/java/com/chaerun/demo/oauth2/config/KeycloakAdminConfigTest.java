package com.chaerun.demo.oauth2.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.chaerun.demo.oauth2.model.KeycloakAdminProperties;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;

class KeycloakAdminConfigTest {

  @Test
  void keycloakAdminClient_createsNonNullClient() {
    KeycloakAdminProperties props = new KeycloakAdminProperties(
        "http://localhost:8080",
        "master",
        "target-realm",
        "client-id",
        "secret");

    KeycloakAdminConfig cfg = new KeycloakAdminConfig(props);
    Keycloak client = cfg.keycloakAdminClient();

    try {
      assertNotNull(client, "Keycloak client should not be null");
    } finally {
      try {
        client.close();
      } catch (Exception ignored) {
        // ignored
      }
    }
  }

}
