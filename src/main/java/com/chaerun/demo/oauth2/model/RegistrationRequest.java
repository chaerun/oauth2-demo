package com.chaerun.demo.oauth2.model;

public record RegistrationRequest(
    String username,
    String email,
    String firstName,
    String lastName,
    String password) {
}
