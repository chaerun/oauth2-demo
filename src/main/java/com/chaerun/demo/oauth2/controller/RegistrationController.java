package com.chaerun.demo.oauth2.controller;

import com.chaerun.demo.oauth2.model.RegistrationRequest;
import com.chaerun.demo.oauth2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegistrationController {

  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest request) { //
    try {
      userService.createUser(request);
      return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

}
