package com.chaerun.demo.oauth2.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import com.chaerun.demo.oauth2.model.RegistrationRequest;
import com.chaerun.demo.oauth2.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class RegistrationControllerTest {

  @Mock
  private UserService userService;

  private RegistrationController controller;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    controller = new RegistrationController(userService);
  }

  @Test
  void registerUser_success_returnsCreated() {
    RegistrationRequest req = new RegistrationRequest("u1", "u1@example.com", "F", "L", "pw");
    // default: do nothing
    ResponseEntity<?> resp = controller.registerUser(req);
    assertEquals(201, resp.getStatusCode().value());
  }

  @Test
  void registerUser_failure_returnsBadRequest() {
    RegistrationRequest req = new RegistrationRequest("u2", "u2@example.com", "F", "L", "pw");
    doThrow(new RuntimeException("fail")).when(userService).createUser(any());

    ResponseEntity<?> resp = controller.registerUser(req);
    assertTrue(resp.getStatusCode().is4xxClientError());
  }
}
