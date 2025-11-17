package com.chaerun.demo.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  /**
   * SecurityFilterChain for Stateless API endpoints (/api/**). This chain has HIGHER precedence (@Order(1)) and is
   * configured as a stateless OAuth2 Resource Server.
   */
  @Bean
  @Order(1)
  SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher(PathPatternRequestMatcher.withDefaults().matcher("/api/**"))
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/api/public")
            .permitAll()
            .anyRequest()
            .authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(Customizer.withDefaults()) // Use JWT validation
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .csrf(csrf -> csrf.disable()); // CSRF is not required for stateless APIs
    return http.build();
  }

  /**
   * SecurityFilterChain for Stateful Web application endpoints (/**). This chain has LOWER precedence (@Order(2)) and
   * is configured for stateful, session-based OAuth2 Login (Authorization Code Flow).
   */
  @Bean
  @Order(2)
  SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher(PathPatternRequestMatcher.withDefaults().matcher("/**"))
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/webjars/**")
            .permitAll() // Whitelist public pages, including login and webjars
            .anyRequest()
            .authenticated() // All other paths require login
        )
        .oauth2Login(oauth2 -> oauth2.loginPage("/login")) // Point to the custom login page
        .logout(logout -> logout
            .logoutSuccessUrl("/") // Redirect to home page on logout
        );
    return http.build();
  }

}
