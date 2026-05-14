# oauth2-demo

A small demo showing OAuth 2.0 with Spring Boot and Keycloak as the identity provider.

This project demonstrates two common OAuth 2.0 grant types and how they are used in real applications:

- Authorization Code Grant (interactive/user): used by the browser-based, stateful web application (`demo-web-client`) to authenticate users via Keycloak (OpenID Connect). Tokens are issued to a user session after the user logs in and grants consent. This flow is implemented with Spring Security's `oauth2Login` and can be seen via the web UI routes such as `/login` and `/profile`.

- Client Credentials Grant (machine-to-machine): used by stateless services or backend processes (for example `demo-api-client` and `demo-admin-client`) to obtain access tokens for the client itself (not a user). These tokens are appropriate for calling protected APIs such as `/api/data` or for server-side calls to the Keycloak Admin API (used by the backend registration logic).

Why include both flows?

- The demo shows how a single identity provider (Keycloak) can handle both interactive user authentication and server-to-server authorization. It also shows how to protect an API with JWTs (resource server) and how to inspect the `azp` (authorized party / client id) claim in tokens.

**Client credentials (for convenience)**

| Client ID         | Client Secret                      |
| ----------------- | ---------------------------------- |
| demo-web-client   | `FFUCSXhGUofYrbKoDnSNsEgKYID46LrX` |
| demo-api-client   | `3wyfe8Uaa6TFAPydVyq29DnLoro7wZBe` |
| demo-admin-client | `6WROqpEYBUq125U3Q1NVQAYgKVzTrIzm` |

**Insomnia collection**

This repository includes an Insomnia export you can import to try the API and authentication flows: `Insomnia_oauth2-demo.yaml` (project root).

To import the collection into Insomnia:

- Open Insomnia → Dashboard → Data → Import/Export → Import Data → From File.
- Select `Insomnia_oauth2-demo.yaml` from this repository.
- Update any environment values as needed (base URL, client secrets).

## Quick start

**Note:** The repo includes `compose.yaml` so Keycloak and its Postgres database can be started with Docker Compose. The project uses `spring-boot-docker-compose` to start Docker Compose automatically when the application runs.

1. Start the Spring Boot app:

```bash
mvn spring-boot:run
```

2. Register a user (required before trying the Authorization Code flow):

- Option A — Use the web UI:

  - Open http://localhost:8080/register and create an account.
  - After successful registration you'll be redirected to the login page.

- Option B — Use the API (example curl):

```bash
# Create a user via the backend registration endpoint
curl -X POST \
  "http://localhost:8080/register" \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","email":"alice@example.com","firstName":"Alice","lastName":"User","password":"password"}'
```

**Authorization Code grant (browser)**

- Open http://localhost:8080 and use the login page to authenticate via Keycloak (use the user you created).
- Visit http://localhost:8080/profile to view the user claims returned by the Authorization Code flow.

**Client Credentials grant (machine-to-machine)**

- Obtain a token from Keycloak's token endpoint (example for `demo-api-client`):

```bash
# Request a client_credentials token for demo-api-client
curl -X POST \
  "http://localhost:9090/realms/oauth2-demo/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials&client_id=demo-api-client&client_secret=3wyfe8Uaa6TFAPydVyq29DnLoro7wZBe"
```

- Use the returned access token to call the protected API:

```bash
curl -H "Authorization: Bearer <ACCESS_TOKEN>" http://localhost:8080/api/data
```

- The API returns JSON that includes `clientId` (from the `azp` claim), showing which client obtained the token.

## Keycloak configuration (optional)

Note: These steps are performed automatically if you run `docker compose up`, which imports `oauth2-demo-realm.json` into Keycloak.

To start Keycloak with Docker Compose:

```bash
docker compose up -d
```

1. Log in to the admin console at http://localhost:9090 (user: `admin`, pass: `admin`).
2. Create a new realm named `oauth2-demo`.
3. In the realm, add any roles the application needs (for example, `ROLE_USER`, `ROLE_ADMIN`).

### Client configuration

#### Client 1 — Web App (Authorization Code Flow)

- Create a new client in Keycloak:
  - Client type: `OpenID Connect`
  - Client ID: `demo-web-client`
  - Client authentication: `On` (confidential client)
  - Standard flow: `On` (enables Authorization Code Grant)
  - Direct access grants: `Off`
- Login settings — Valid redirect URIs:
  - `http://localhost:8080/login/oauth2/code/keycloak` (Spring Security default)
  - `http://localhost:8080/*`
- Save and copy the Client Secret (used in `application.yml`).

#### Client 2 — Service API (Client Credentials Flow)

- Create a client with these settings:
  - Client type: `OpenID Connect`
  - Client ID: `demo-api-client`
  - Client authentication: `On`
  - Standard flow: `Off`
  - Direct access grants: `Off`
  - Service accounts roles: `On` (enables Client Credentials Grant)
- Save and copy the Client Secret.

#### Client 3 — Backend Admin (Admin API Access)

- Create a client with these settings:
  - Client type: `OpenID Connect`
  - Client ID: `demo-admin-client`
  - Client authentication: `On`
  - Standard flow: `Off`
  - Direct access grants: `Off`
  - Service accounts roles: `On`
  - Save the client.
    - In the client settings, go to `Service Account Roles` and assign the `manage-users` role (and `view-users` if needed).
    - Copy the Client Secret for server-side use.

## Integrating social logins (Keycloak as an identity broker)

### Google

Step 1: Google Cloud Console

1. Navigate to the Google Cloud Console (console.cloud.google.com).
2. Go to **APIs & Services** > **Credentials**.
3. Configure the **OAuth consent screen** if not already done.
4. Click **Create Credentials** > **OAuth client ID**.
5. Select **Web application** as the type.
6. In **Authorized redirect URIs**, add the Keycloak broker callback URL. This URI is found on the Keycloak "Add Google" provider page and follows the format: `http://localhost:9090/realms/spring-boot-app/broker/google/endpoint`.
7. Click **Create** and copy the **Client ID** and **Client Secret**.

Step 2: Keycloak Admin Console

1. In the `oauth2-demo` realm, navigate to **Identity Providers**.
2. Select **Google** from the list of social providers.
3. Paste the **Client ID** and **Client Secret** obtained from Google.
4. Click **Save**.

### GitHub

Step 1: GitHub Developer Settings

1. Navigate to GitHub **Settings** > **Developer settings** > **OAuth Apps**.
2. Click **New OAuth App**.
3. Set **Homepage URL** to `http://localhost:8080`.
4. Set **Authorization callback URL** to the Keycloak broker callback: `http://localhost:9090/realms/spring-boot-app/broker/github/endpoint`.
5. Click **Register application**.
6. Generate a new **Client Secret** and copy both the **Client ID** and the new **Client Secret**.

Step 2: Keycloak Admin Console

1. Navigate to **Identity Providers** in the Keycloak realm.
2. Select **GitHub** from the list.
3. Paste the **Client ID** and **Client Secret** obtained from GitHub.
4. Click **Save**.

## Spring Boot project setup

Create a new Spring Boot project (https://start.spring.io/) with these dependencies:

- Spring Web
- Spring Security
- OAuth2 Client (for `oauth2Login`)
- OAuth2 Resource Server (for API token validation)
- Thymeleaf

Optional developer tools:

- Lombok
- Spring Boot DevTools
- Spring Configuration Support
- Docker Compose Support

### Run the application

```bash
mvn spring-boot:run
```

If you need help running the demo or updating configuration values (for example, client secrets or Keycloak URLs), tell me what you want to change and I will update the README or config files accordingly.
