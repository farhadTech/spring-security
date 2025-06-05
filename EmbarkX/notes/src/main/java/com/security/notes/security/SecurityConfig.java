package com.security.notes.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                .anyRequest().authenticated());
//        http.formLogin(withDefaults());
        http.csrf(csrf -> csrf.disable());
        http.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager();

        UserDetails user = User.withUsername("user1")
                .password("{noop}password1")
                .roles("USER")
                .build();

        if (!manager.userExists("user1")) {
            manager.createUser(user);
        }

        UserDetails admin = User.withUsername("admin")
                .password("{noop}adminPass")
                .roles("ADMIN")
                .build();

        if (!manager.userExists("admin")) {
            manager.createUser(admin);
        }
        return manager;
    }
}
