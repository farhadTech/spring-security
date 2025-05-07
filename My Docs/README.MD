# 🍀Spring Security Architecture🔏



Wait!!👻🫸 Before diving deep into Spring Security architecture first we need to know about Filters in Spring.
## Filters in Spring🔎🔎
***
***Overview***
* What are Filters?
* Understanding the Filter Chain.
* Implementing a Custom Filter.
***

## ➡️ What are Filters?
> **🌿A filter is part of the Servlet API. It sits between the client and our Spring Application.** <br>
_For Example it serves as a gatekeeper for our application. For each request they validate the request or perform some kind of operations on that filter._
<br>

***Here is a Diagram***
![](images/filter_01.png)
***
🥇 First our Service is deployed on the server like Tomcat/Jetty/UnderTow. <br>
🥈 Then Servlet API Handles request and responses. It typically responds to HTTP request coming from web browsers or REST clients.<br>
🥉 Then it goes to Spring application, we found Dispatcher Servlet. Which is basically abstraction of Servlet API. This Dispatcher Servlet sits inside the Spring core.
***

## Use Case of Filter 🪴
***
🔰 Logging request or response details  
🔰 Modification request or response details  
🔰 Authentication and authorization  
🔰 Request or input validation or modification  
🔰 CORS configuration  
***

## Understanding Filter Chain ⛓️
***What is Happening inside Servlet API***

![](images/filter_02.png)
***
🏫 Now request goes to loggingFilter then it goes to AuthenticationFilter afterward it goes to ValidationFilter, because this is a filter chain⛓️ it gradually checks all the filter then it goes to Dispatcher Servlet.<br>
📔 Filter Chain in nothing but chain of filters which are running in sequential manner.
***

## Implementing a Custom Filter
```java
@component  // Marks this class as a Spring component so that Spring Boot can detect and register it as a bean
public class LoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, 
                         ServletResponse servletResponse,
                         FilterChain filterChain) {
        
        // Cast the generic ServletRequest to HttpServletRequest to access HTTP-specific methods like getRequestURI().
        private HttpServletRequest httpServletRequest = (HttpServletRequest) ServletRequest;
        
        // Print the URI of the incoming request to the console (for logging/debugging purposes).
        System.out.println("Request URL: " + httpServletRequest.getRequestURI());
        
        // Continue the filter chain. This means the request is passed to the next filter (if any),
        // or to the target servlet/controller fi there are no more filters.
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
```

## Spring Security Architecture
***
***Overview***
* What is Spring Security
* Authentication
* Authorization
* Architecture
***

## ➡️ What is Spring Security?
> 🌿 It helps protect APIs, WebApps and Microservices from unauthorized access, attacks and security threats.

***Key Features:***
***
1️⃣ Authentication - Verifies who the user is (e.g., login form, HTTP basic, JWT).  
2️⃣ Authorization - Determines what the user is allowed to do (e.g., role-based access).  
3️⃣ Password Encoding - Securely hashes and verifies user passwords.  
4️⃣ CSRF Protection - Prevents cross-site request forgery attacks.  
5️⃣ Session Management - Controls how user sessions are handled and protected.  
6️⃣ Security Filters - Uses a filter chain to apply secuirty checks to every request.  
7️⃣ Method-level Security - Use annotations like `@PreAuthorize` and `@Secured` to secure specific methods.  

***
Common Use Cases:
* Protecting REST APIs.  
* Securing login/logout flows.  
* Managing roles like `ADMIN`, `USER`, etc.  
* Integrating with JWT, OAuth2, or LDAP.  
***

**Example:**
In a Spring Boot app, adding this to your `SecurityConfig` class can secure your endpoints:

```java
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin();
    }
}
```

## What is Authentication?
> 🌿 Authentication is the process of verifying the identity of a user.  
> In simpler terms, it's about answering the question:   
>  ***who you are?***  
> It ensures that user is legitimate before granting access.  

***In the Context of Web Applications:***
***
***Authentication is what happens when a user logs in with credentials like:***
* Username & Password
* JWT token
* OAuth token (Google, Facebook, etc.)
* API key
***
If the credentials are correct, the user is considered authenticated and allowed to access protected resources (depending on authorization rules).

***Example in Spring Security:***
In a Spring Boot app, adding this to your `SecurityConfig` class can secure your endpoints:
```java
httpSecurity
        .authorizeRequests()
        .anyRequest().authenticated()
        .and()
        formLogin();
```
*** 
***Spring Security will:***
1. Show the login page.
2. Take the username/password
3. Authenticate the user (using `UserDetailsService`).
4. Store user details in the session.
***

## What is Authorization?
> 🌿 Authorization is the process of determining what an authenticated user is allowed to do.  
> It answers the question:  
> "What can you access?"  

While **Authentication** confirms identity (e.g., you are "admin@example.com"),
**authorization** checks what permissions or roles you have (e.g., you can delete users, but not access finacial records).

***Example in Spring Security:**
Suppose you want only users with the role `ADMIN` to access the `/admin` endpoint.
```java
httpSecurity
        .authorizeRequests()
        .antMatchers("/admin/**").hasRole("ADMIN")
        .anyRequest().authenticated();
```
Here:
* If a user is authenticated but does not have the `ADMIN` role, access will be denied.
* Authorization checks happen after authentication is successful

***Common Types of Authorization:***
***
* Role-based access control (RBAC) -e.g., roles like `USER`, `MODERATOR`, `ADMIN`.
* Permission-based access control - fine-grained control (e.g. `CAN_EDIT_USER`).
* Method-level authorization - annotations like:
***

```java
@PreAuthorize("hasRole('ADMIN'")
public void deleteUser(Long id) {
    ...
}
```
# 🍀Spring Security Architecture 🔐

## DelegatingFilterProxy
> Spring provides a `Filter` implementation named `DelegatingFilterProxy` that allows bridging between the Servlet container's lifecycle and Spring's ApplicationContext.
> The Servlet container allows registering `Filter` instances by using its own standards, but it is not aware of Spring-defined Beans.
> We can register `DelegatingFilterProxy through` the standard Servlet container mechanism but delegate all the work to a Spring Bean that implements `Filter`.  
  <br>
  <br>
![delgating filter proxy](/images/delegating_filter_proxy.png)

***
_When we add Spring Boot Starter Security dependency then a `SecurityFilterChain` get added. This happens when we add support for spring security.
Servlet API has separate lifecycle and Spring Bean's have separate life cycle too. We want bridge between these two. 
So, we want to connect our Servlet API lifecycle to our Spring application context. That is when `DelegatingFilterProxy` comes into picture._  

***Image of `DelegatingFilterProxy` connecting From Servet to Application***  
![Spring Security](/images/spring_security_part_01.png)
***

## FilterChainProxy
> Spring Security's Servlet support is contained within `FilterChainProxy`. 
> `FilterChainProxy` is a special `Filter` provided by Spring Security that allows delegating to many `Filter` instances through `SecurityFilterChain`.  
> Since `FilterChainProxy` is a Bean, it is typically wrapped in a `DelegatingFilterProxy`.

***The Following image shows the role of `FilterChainProxy`***.  

![filter chain proxy](/images/filter_chain_proxy.png)

_So We have several filter connecting from client to servlet like this. One of the filter is `DelegatingFilterProxy` which has `FilterChainProxy` implements `SecurityFilterChain` like this._  
_`FilterChainProxy` is a special filter that will be added inside our `DelegatingFilterProxy` and this have `SecurityFilterChain` and this all in our `FilterChain`._
## SecurityFilterChain
> `SecurityFilterChain` is used by `FilterChainProxy` to determine which Spring Security `Filter` instances should be invoked for the current request.

> The Security Filters in `SecurityFilterChain` are typically Beans, but they are registered with `FilterChainProxy` instead of `DelegatingFilterProxy`. 
> `FilterChainProxy` provides a number of advantages to registering directly with the Servlet container or `DelegatingFilterProxy`.
> First, it provides a starting point for all of Spring Security's Servlet support.
***
The following image shows the role of `SecurityFilterChain`.  
<br>
![SecurityFilterChain](/images/security_filter_chain.png)
***

_Our `FilterChain` have `FilterChainProxy` Which will have `SecurityFilterChain`. In `SecurityFilterChain` there will be multiple Security Filters. Starting from filter<sub>0</sub> to Filter<sub>n</sub> .
`DelegatingFilterProxy` and `FilterChainProxy` is abstract for us. We rather use `SecurityFilterChain` directly when we try to log in using username and password._  
***
***Here is a Picture of `SecurityFilterChain` reside in `FilterChain`***
![SecurityFilterChain02](/images/SecurityFilterChain_02.png)
***

## AuthenticationProvider
> In Spring Security, an `AuthenticationProvider` is a core interface used to perform **authentication logic**. 
> It is responsible for verifying the credentials (like username and password) provided in an authentication request and returning a fully authenticated object if the authentication is successful.

### Interface Definition
```java
public interface AuthenticationProvider {
    Authentication authenticate(Authentication authentication) throws AuthenticationException;
    boolean supports(Class<?> authentication);
}
```

### How it Works
1. `authenticate()`:  
  * Takes an `Authentication` object (e.g., `UsernamePasswordAuthenticationToken`) that contains credentials.
  * Validates the credentials (e.g., mathes the password).
  * If valid, returns an authenticated `Authentication` object with user details and roles.
  * If invalid, throws and `AuthenticationException`.

2. `supports()`:  
  * Determines whether this provider supports the given `Authentication` class.

### Common Implementation: `DaoAuthenticationProvider`
Spring Security provides a buit-in-implementation called `DaoAuthenticationProvider` which is used with `UserDetailsService`:

```java
@Bean
public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    return provider;
}
```
* `UserDetailsService`: Loads user data from DB.
* `PasswordEncoder`: Verifies encrypted passwords.

### Custom `AuthenticationProvider` Example:
If you want custom authentication logic (e.g., token-based login):
```java
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    
    @Override
  public Authentication authenticate (Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        // Custom logic (e.g., checking from database or external service)
      if("admin".equals(username) && "secret".equals(password)) {
          return new UsernamePasswordAuthenticationToken(username, password, List.of(new SimpleGrantedAuthority("ROLE_USER")));
      } else {
          throw new BadCredentialsException("Authentication failed");
      }
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
```

You register it in the security config using:
```java
http.authenticationProvider(customAuthenticationProvider);
```
## Built-in `AuthenticationProvider` Implementations in Spring Security
1. `DaoAuthenticationProvider`
  * Most commonly used.
  * Authenticates using a `UserDetailsService` and a `PasswordEncoder`.
  * Used in username-password login forms.
  * Ideal for form-based or basic authentication.

```java
DaoAuthenticaionProvider provider = new DaoAuthenticationProvider();
provider.setuserDetailsService(userDetailsService);
provider.setPasswordEncoder(passwordEncoder);
```
2. `LdapAuthenticationProvider`
  * Used for LDAP-based authentication(e.g., corporate Active Directory).
  * Connects to LDAP servers to fetch and validate user credentials.

3. `JwtAuthenticationProvider` (not built-in but common in JWT setups)
  * Custom implementations for JWT token-based authentication.
  * Validates JWT tokens and extracts user information.

Spring Security doesn't provide this out-of-the-box--you write a custorm provider or use filters.

4. `CasAuthenticationProvider`
  * For CAS (Central Authentication Service) integration.
  * Used in SSO (Single Sign-On) setups.

5. `OpenIDAuhtenticationProvider` (Deprecated)
  * Used for OpenID-based authentication (now largely replaced by OAuth2 / OIDC).
  * Deprecated in favor of more modern standards.

6. `OAuth2LoginAuthenticationProvider`:
  * Handles **OAuth2 login** (e.g., Google, Facebook).
  * Used internally in Spring Security when you configure `.oauth2login()`.

7. `Saml2AuthenticationProvier`:
  * Supports SAML 2.0 protocol for enterprise-level SSO authentication.
  * Often used with identity providers like Okta, ADFS.

**Custom** `AthenticationProvider`
You can define your own if the built-in ones don't meet your needs:
  * Token-based systems
  * SMS/email OTP
  * Biometric verification


 When we try to log in we need to give username and password. Initially username and password is generated by Spring.
This generated username and password is for development purpose only. We should not use in production.  
When we use in production then we should make use any of the `AuthenticationProvider` we discussed earlier.

Some of the common authentication is OAuth2, JWT, and LDAP.  
For each of the authentication like InMemory, OAuth2, LDAP Spring provide own `AuthenticationProvider`.

![SecurityFilterChain02](/images/username_and_password_authentication_filter.png)



When we use in 

## 🤷‍♂️🤷 What is Servlet API?


