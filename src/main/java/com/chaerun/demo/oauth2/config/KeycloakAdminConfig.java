package com.chaerun.demo.oauth2.config;

import com.chaerun.demo.oauth2.model.KeycloakAdminProperties;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KeycloakAdminProperties.class)
@RequiredArgsConstructor
public class KeycloakAdminConfig {

  private final KeycloakAdminProperties props;

  @Bean
  Keycloak keycloakAdminClient() {
    return KeycloakBuilder.builder()
        .serverUrl(props.serverUrl())
        .realm(props.realm())
        .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
        .clientId(props.clientId()) // Use 'demo-admin-client' service account
        .clientSecret(props.clientSecret())
        .build();
  }

}
