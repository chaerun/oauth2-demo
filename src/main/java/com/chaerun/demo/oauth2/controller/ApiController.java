package com.chaerun.demo.oauth2.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class ApiController {

    @GetMapping("/api/data")
    public Map<String, Object> getApiData(@AuthenticationPrincipal Jwt jwt) {
        return Map.of(
            "message", "Hello from a protected API",
            "clientId", jwt.getClaimAsString("azp"), // Client ID
            "issuer", jwt.getIssuer().toString()
        );
    }
}
