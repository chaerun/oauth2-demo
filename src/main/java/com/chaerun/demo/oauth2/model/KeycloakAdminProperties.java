package com.chaerun.demo.oauth2.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak.admin")
public record KeycloakAdminProperties(
    String serverUrl,
    String realm,
    String targetRealm,
    String clientId,
    String clientSecret) {
}
