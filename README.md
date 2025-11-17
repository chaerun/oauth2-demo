# oauth2-demo

Demo OAuth 2.0 Authorization with Spring Boot and Keycloak as an Identity Provider

## Keycloak Configuration (Prerequisites)

> **Note: These steps are automatically configured when running `docker compose up`, by importing `oauth2-demo-realm.json`.**

Start Keycloak server with Docker:

```bash
docker compose up -d
```

1. Log in to the admin console at `http://localhost:9090` (user: `admin`, pass: `admin`).
2. Create a new Realm: `oauth2-demo`.
3. Once created, navigate to **Realm Roles** and create the roles the application will use, such as `ROLE_USER` and `ROLE_ADMIN`.

### Client 1: The Web App Client (Authorization Code Flow)

This client represents the stateful, user-facing Spring Boot web application. It will use the Authorization Code Grant, which is designed for interactive user sessions.

- Navigate to `Clients` > `Create client`
- In **General settings**:
  - Client type: `OpenID Connect`
  - Client ID: `demo-web-client`
- In **Capability config**:
  - Client authentication: `On`.
    > This designates it as a "confidential" client, meaning it must provide a secret to exchange the code for a token.
  - Authentication flow:
    - **Standard flow**: `On`
      > This explicitly enables the 'Authorization Code Grant'.
    - **Direct access grants**: `Off` (uncheck)
      > This disables support of 'Resource Owner Password Credentials Grant'.
- In **Login settings**:
  - Valid redirect URIs:
    - `http://localhost:8080/login/oauth2/code/keycloak` (This is the Spring Security default).
    - `http://localhost:8080/*`
- **Save** the client.
- Go to the **Credentials** tab and copy the **Client secret**. You'll need this for your `application.yml`.

### Client 2: The Service API Client (Client Credentials Flow)

This client represents a stateless, machine-to-machine (M2M) service. It will use the Client Credentials Grant, which allows an application to obtain a token on behalf of itself, not a user.

- Navigate to `Clients` > `Create client`
- In **General settings**:
  - Client type: `OpenID Connect`
  - Client ID: `demo-api-client`
- In **Capability config**:
  - Client authentication: `On`
  - Authentication flow:
    - **Standard flow**: `Off` (uncheck)
      > This disables the 'Authorization Code Grant'.
    - **Direct access grants**: `Off` (uncheck)
      > This disables support of 'Resource Owner Password Credentials Grant'.
    - **Service accounts roles**: `On`
      > This enables 'Client Credentials Grant'.
- **Save** the client.
- Go to the **Credentials** tab and copy the **Client secret**.

### Client 3: The Backend Admin Client (Admin API Access)

This third client is required to fulfill the custom registration page requirement. The Spring Boot backend needs its own identity to authenticate against the Keycloak Admin API and create users.

- Navigate to `Clients` > `Create client`
- In **General settings**:
  - Client type: `OpenID Connect`
  - Client ID: `demo-admin-client`
- In **Capability config**:
  - Client authentication: `On`
  - Authentication flow:
    - **Standard flow**: `Off` (uncheck)
      > This disables the 'Authorization Code Grant'.
    - **Direct access grants**: `Off` (uncheck)
      > This disables support of 'Resource Owner Password Credentials Grant'.
    - **Service accounts roles**: `On`
      > This enables 'Client Credentials Grant'.
- **Save** the client.
- Navigate to the `Service Account Roles` tab for this `demo-admin-client`.
- Click the **Assign Role** button.
- From the Available Roles, assign `manage-users` (and `view-users` if needed) to the Assigned Roles. This grants the client the necessary permissions.
- Go to the **Credentials** tab and copy the **Client secret**.

## Integrating Social Logins: Keycloak as an Identity Broker

### Configuration for Google

**Step 1: Google Cloud Console**

1. Navigate to the Google Cloud Console (console.cloud.google.com).
2. Go to `APIs & Services` > `Credentials`.
3. Configure the `OAuth consent screen` if not already done.
4. Click `Create Credentials` > `OAuth client ID`.
5. Select Web application as the type.
6. In **Authorized redirect URIs**, add the Keycloak broker callback URL. This URI is found on the Keycloak "Add Google" provider page and follows the format: `http://localhost:9090/realms/spring-boot-app/broker/google/endpoint`.
7. Click `Create` and copy the `Client ID` and `Client Secret`.

**Step 2: Keycloak Admin Console**

1. In the `oauth2-demo` realm, navigate to `Identity Provider`s.
2. Select `Google` from the list of social providers.
3. Paste the `Client ID` and `Client Secret` obtained from Google.
4. Click `Save`.

### Configuration for GitHub

**Step 1: GitHub Developer Settings**

1. Navigate to GitHub `Settings` > `Developer settings` > `OAuth Apps`.
2. Click `New OAuth App`.
3. Set **Homepage URL** to `http://localhost:8080`.
4. Set **Authorization callback URL** to the Keycloak broker callback: `http://localhost:9090/realms/spring-boot-app/broker/github/endpoint`.
5. Click `Register application`.
6. Generate a new `Client Secret` and copy both the `Client ID` and the new `Client Secret`.

**Step 2: Keycloak Admin Console**

1. Navigate to `Identity Providers` in the Keycloak realm.
2. Select `GitHub` from the list.
3. Paste the `Client ID` and `Client Secret` obtained from GitHub.
4. Click `Save`.

## Spring Boot Project Setup

Create a new project at [start.spring.io](https://start.spring.io/) with these dependencies:

- Spring Web
- Spring Security
- OAuth2 Client (for `oauth2Login`)
- OAuth2 Resource Server (for API token validation)
- Thymeleaf (to create simple web pages)

Optional `developer tools` dependencies:

- Lombok
- Spring Boot DevTools
- Spring Configuration Support
- Docker Compose Support

### Start `oauth2-demo` Application

```bash
mvn spring-boot:run
```
