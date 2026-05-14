package com.chaerun.demo.oauth2.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.chaerun.demo.oauth2.model.KeycloakAdminProperties;
import com.chaerun.demo.oauth2.model.RegistrationRequest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserServiceTest {

  @Mock
  private Keycloak keycloak;
  @Mock
  private RealmResource realmResource;
  @Mock
  private UsersResource usersResource;
  @Mock
  private Response response;

  private KeycloakAdminProperties props;
  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    props = new KeycloakAdminProperties("http://localhost:8080", "master", "target-realm", "client", "secret");
    userService = new UserService(keycloak, props);
  }

  @Test
  void createUser_success_noException() {
    RegistrationRequest req = new RegistrationRequest("u1", "u1@example.com", "First", "Last", "pass123");

    when(keycloak.realm(props.targetRealm())).thenReturn(realmResource);
    when(realmResource.users()).thenReturn(usersResource);
    when(usersResource.create(any())).thenReturn(response);
    when(response.getStatus()).thenReturn(201);

    assertDoesNotThrow(() -> userService.createUser(req));
  }

  @Test
  void createUser_failure_throwsIllegalStateException() {
    RegistrationRequest req = new RegistrationRequest("u2", "u2@example.com", "F", "L", "pw");

    when(keycloak.realm(props.targetRealm())).thenReturn(realmResource);
    when(realmResource.users()).thenReturn(usersResource);
    when(usersResource.create(any())).thenReturn(response);
    when(response.getStatus()).thenReturn(400);
    when(response.readEntity(String.class)).thenReturn("User exists");

    assertThrows(IllegalStateException.class, () -> userService.createUser(req));
  }
}
