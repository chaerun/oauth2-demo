# oauth2-demo

Demo OAuth 2.0 Authorization with Spring Boot and Keycloak as an Identity Provider

## Keycloak Configuration (Prerequisites)

Start Keycloak server with Docker:

```bash
docker compose up -d
```

1. Log in to the admin console at `http://localhost:8080` (user: `admin`, pass: `admin`).
2. Create a new Realm: `oauth2-demo`.

### Client 1: The Web App (Authorization Code)

This client represents your Spring Boot application's web front end.

- Go to `Clients` > `Create client`
- In **General settings**:
  - Client type: `OpenID Connect`
  - Client ID: `demo-web-app`
- In **Capability config**:
  - Client authentication: `On`
  - Authorization: `On`
  - Authentication flow: Keep defaults (`Standard flow`).
- In **Login settings**:
  - Valid redirect URIs: `http://localhost:8081/login/oauth2/code/keycloak` (This is the Spring Security default).
- **Save** the client.
- Go to the **Credentials** tab and copy the **Client secret**. You'll need this for your `application.yml`.

### Client 2: The API Client (Client Credentials)

This client represents a machine or service that will call your API.

- **Go to** `Clients` > `Create client`
- In **General settings**:
  - Client type: `OpenID Connect`
  - Client ID: `demo-api-client`
- In **Capability config**:
  - Client authentication: `On`
  - Authorization: `On`
  - Authentication flow: Uncheck "Standard flow" and check `Service accounts roles` (this enables Client Credentials).
- **Save** the client.
- Go to the **Credentials** tab and copy the **Client secret**.

### Roles and Users

We need one role for web users and one for API clients.

1. Create Realm Roles:
   - Go to `Realm roles` > `Create role`.
   - Create a role named `USER`.
   - Create another role named `API_CLIENT`.
2. Create a Web User:
   - Go to `Users` > `Create user`.
   - Username: `demo`
   - Go to the **Credentials** tab and set a password (e.g., `demo`).
   - Go to the **Role mapping** tab, click **Assign role**, and assign the `USER` role.
3. Assign API Role to Service Account:
   - Go to **Clients** > `demo-api-client`.
   - Go to the **Service account roles** tab.
   - Click **Assign role** and assign the `API_CLIENT` role.

## Spring Boot Project Setup

Create a new project at [start.spring.io](https://start.spring.io/) with these dependencies:

- Spring Web
- Spring Security
- OAuth2 Client (for `oauth2Login`)
- OAuth2 Resource Server (for API token validation)
- Thymeleaf (to create simple web pages)
