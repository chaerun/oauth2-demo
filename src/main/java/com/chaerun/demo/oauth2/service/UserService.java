package com.chaerun.demo.oauth2.service;

import com.chaerun.demo.oauth2.model.KeycloakAdminProperties;
import com.chaerun.demo.oauth2.model.RegistrationRequest;
import jakarta.ws.rs.core.Response;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final Keycloak keycloakAdminClient;
  private final KeycloakAdminProperties props;

  public void createUser(RegistrationRequest request) {
    // 1. Define Password Credentials
    CredentialRepresentation passwordCred = new CredentialRepresentation();
    passwordCred.setTemporary(false);
    passwordCred.setType(CredentialRepresentation.PASSWORD);
    passwordCred.setValue(request.password());

    // 2. Define User Representation
    UserRepresentation user = new UserRepresentation();
    user.setUsername(request.username());
    user.setEmail(request.email());
    user.setFirstName(request.firstName());
    user.setLastName(request.lastName());
    user.setEnabled(true);
    user.setEmailVerified(false); // Can be set to false, with email verification flow
    user.setCredentials(Collections.singletonList(passwordCred));

    // 3. Get Realm Resource (for the target realm) and Create User
    RealmResource realmResource = keycloakAdminClient.realm(props.targetRealm());
    try (Response response = realmResource.users().create(user)) { //
      if (response.getStatus() != 201) {
        // Read response body for error details (e.g., user exists)
        String error = response.readEntity(String.class);
        throw new IllegalStateException("Failed to create user: " + error);
      }
    }
  }

}
