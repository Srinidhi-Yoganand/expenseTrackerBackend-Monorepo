# Kong API Gateway with Custom Authentication Plugin

## Overview

This project sets up an API gateway using **Kong**, a powerful open-source API Gateway and Microservices Management Layer, to handle multiple services. This setup involves several backend services and uses a **Custom Authentication Plugin** to secure the API routes by verifying the authorization of incoming requests against an external authentication service.

This project consists of:

* **Kong Gateway** that handles incoming requests, forwards them to the appropriate service, and applies security plugins.
* **Custom Authentication Plugin** that intercepts the requests, calls an external authentication service to verify the authorization, and ensures only authorized users can access the APIs.
* Multiple **microservices** that are proxied through Kong with various routes.

---

## Table of Contents

1. [Architecture Overview](#architecture-overview)
2. [How It Works](#how-it-works)
3. [Setting Up and Running](#setting-up-and-running)
4. [Using the Service](#using-the-service)
5. [Plugin Customization](#plugin-customization)
6. [Troubleshooting](#troubleshooting)
7. [License](#license)

---

## Architecture Overview

### Key Components:

1. **Kong API Gateway**: Acts as a reverse proxy, managing routes and traffic. Kong handles incoming HTTP requests, applies relevant plugins, and forwards the requests to the correct microservices.

2. **Custom Authentication Plugin**: This plugin is a Lua-based middleware that intercepts incoming requests, retrieves the authorization token from the request, and checks it against an external authentication service. If the authentication succeeds, the plugin forwards the request to the relevant service; otherwise, it returns an error.

3. **Microservices**: These are individual backend services, each with a specific route. These services are proxied through Kong and secured with the Custom Authentication Plugin.

---

## How It Works

The system relies on **Kong** to manage API routing and apply middleware (plugins) to secure and control access to the backend services.

### Flow of an API Request:

1. **Incoming Request**: A client sends an HTTP request to Kong’s proxy endpoint.
2. **Kong Routes the Request**: Kong matches the request to the appropriate route and service.
3. **Authentication Check**: The Custom Authentication Plugin is applied to the route. The plugin:

   * Extracts the `Authorization` header from the request.
   * Makes a `GET` request to the authentication service (`auth-service`) to validate the authorization token.
   * If the token is valid, it retrieves user information and passes the request to the backend service.
   * If the token is invalid or not present, the plugin returns a `401 Unauthorized` response.
4. **Forwarding the Request**: If authentication is successful, the request is forwarded to the intended service (e.g., `expense-service`, `parser-service`).
5. **Response**: The backend service processes the request and sends a response back through Kong to the client.

![](images/UML.png)

---

## Setting Up and Running

### Prerequisites:

1. **Docker**: Ensure Docker is installed on your system to run the Kong Gateway and services in containers.
2. **Kong**: The Kong API Gateway is based on Docker. It is configured to use custom plugins and routes to forward requests.

### Steps to Set Up:

1. **Clone the Repository**:

   ```bash
   git clone https://github.com/Srinidhi-Yoganand/kong-APIGateway.git
   cd kong-APIGateway
   ```

2. **Build and Run the Containers**:

   The Dockerfile will build the Kong Gateway with the custom plugin included.

   * Build the Docker image:

     ```bash
     docker build -t kong-custom-plugin .
     ```

   * Start the Kong Gateway with your configuration:

     ```bash
     docker run -d --name kong -p 8000:8000 -p 8443:8443 -p 8001:8001 kong-custom-plugin
     ```

   * Ensure that all backend services (e.g., `auth-service`, `user-service`, `expense-service`) are running on their respective ports (or in Docker containers). You can set up Docker containers for these services using docker-compose or separately.

3. **Configure Kong**: Kong is configured with a YAML file `kong.yml` that defines routes, services, and plugins. This configuration file is loaded into Kong when the container starts.

4. **Start Kong Gateway**: After the services and Kong are configured, the API Gateway will be running, and you can start sending requests to the defined routes.

---

## Using the Service

### Example API Routes

Here are the API endpoints exposed by Kong:

1. **Kong Status**:

   * Route: `/status`
   * Service: `kong-status`
   * This is a simple endpoint used for checking if Kong is up and running.

2. **Expense Service**:

   * Route: `/expense/v1`
   * Service: `expense-service`
   * This service is secured by the custom authentication plugin. Only requests with a valid authorization token will be allowed.

3. **User Service**:

   * Route: `/user/v1`
   * Service: `user-service`
   * This service also uses the custom authentication plugin to ensure only authenticated users can access user-related data.

4. **Parser Service**:

   * Route: `/v1/ds`
   * Service: `parser-service`
   * This service is another backend service that is secured with the custom authentication plugin.

---

### Example Request to `expense-service`:

To access the `expense-service`, you need to include a valid `Authorization` token in the request header.

```bash
curl -X GET http://localhost:8000/expense/v1 \
     -H "Authorization: Bearer <your-token>"
```

* **Success Response**: If the token is valid, the request will be forwarded to the backend service.
* **Failure Response**: If the token is missing or invalid, the response will be a `401 Unauthorized` status with a message: `Unauthorized`.

---

## Plugin Customization

### Custom Authentication Plugin

The **Custom Authentication Plugin** is implemented in Lua and is responsible for checking the authorization token by calling the `auth-service`. The plugin uses the following logic:

1. **Extract Authorization Token**: The plugin extracts the `Authorization` header from the incoming HTTP request.
2. **Request to Auth Service**: It sends a `GET` request to the `auth-service` to verify the token.
3. **Verify Response**: If the `auth-service` responds with a `200 OK`, the token is valid, and the request proceeds. Otherwise, the plugin returns a `401 Unauthorized` response.
4. **Set User Information**: If the token is valid, the plugin adds the `X-User-ID` header to the request to pass the user information to the downstream service.

### Customizing the Plugin:

* **Changing Authentication URL**: You can modify the URL for the `auth-service` by updating the `auth_service_url` in the plugin configuration (`kong.yml`).

---

## Troubleshooting

* **Kong not forwarding requests**: Check if the routes are properly configured in `kong.yml` and the backend services are up and running.
* **Authentication errors**: Ensure that the `auth-service` is correctly validating the token and that the token passed in the request header is valid.
* **Plugin errors**: Check the Kong logs for any issues related to the Lua plugin or external service communication.

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
