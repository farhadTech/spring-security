# Spring Security Architecture Overview
Spring security is built as a filter-based architecture that integrates seamlessly with the Servlet Filter Chain in a Spring web application.

## Core Concepts
1. Authentication - Verifying the identity of a user (e.g., username and password).
2. Authorization - Determining if the user has permission to access a resource.
3. Filter Chain - A series of filters that process the HTTP request/response before it reaches the controller.

# Key Components of Spring Security Architecture
### 1. Security Filter Chain
At the heart of Spring Security is the `SecurityFilterChain`, which contains many filters like:
* `usernamePasswordAuthenticationFilter` - Authenticates user credentials.
* `BasicAuthenticationFilter` - Handles HTTP Basic Authentication.
* `SecurityContextPersistenceFilter` - Loads and stores the `SecurityContext`.
* `ExceptionTranslationFilter` - Handles access denied and authentication errors.
Spring Boot automatically registers these filters into the servlet container.

### 2. AuthenticationManager
The `AuthenticationManager` is the main interface for authentication.
* It receives an `Authentication` object (e.g., with username and password).
* It returns a fully authenticated object if successful, or throws an exception otherwise.

By default, we often user:
```java
AuthenticationManager authenticationManager = new ProviderManager(List.of(myAuthProvider));
```

### 3. AuthenticationProvider
An `AuthenticationProvider` is responsible for validating the `Authentication` request.
* Example: `DaoAuthenticationProvider` (used with `UserDetailsService` and `PasswordEncoder`).

We can configure our own:
```java
public class CustomAuthenticationProvider implements AuthenticationProvider {
  // your logic
}
```

### 4. UserDetailsService & UserDetails
* `UserDetailsService`: Fetches user-specific data from the database.
* `UserDetails`: Contains user info like username, password, roles.

Example:
```java
@Override
public UserDetails loadUserByUsername(String username) {
  return new User(username, password, authorities);
}
```

### 5. SecurityContext & SecurityContextHolder
* Holds the `Authentication` object for the current request.
* `SecurityContextHolder` stores it (in a `ThreadLocal` by default).

We can access it like:
```java
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
```

### 6. AccessDecisionManager & Voters
After authentication, Spring Security uses **AccessDecisionManager** with **Voters** to decide whether access should be granted.
* Common voters: `RoleVoter`, `AuthenticatedVoter`, `WebExpressionVoter`.

### 7. Method Security
We can secure methods using annotations:
```java
@PreAuthorize("hasRole('ADMIN')")
public void secureMethod() {
  // ...
}
```

Enable this in config:
```java
@EnableGlobalMethodSecurity(prePostEnabled = true)
```

## Example Flow of a Request
1. User sends login request (POST/login).
2. `UsernamePasswordAuthenticationFilter` captures credentials.
3. Credentials are passed to `AuthenticationManager`.
4. `AuthenticationManager` delegates to an `AuthenticationProvider`.
5. If successful, `Authentication` is stored in `SecurityContext`.
6. For subsequent requests, the user is authenticated and authorized using roles/permissions.

