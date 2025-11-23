package com.chaerun.demo.oauth2.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

class WebControllerTest {

  private WebController controller;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    controller = new WebController(objectMapper);
  }

  @Test
  void home_returnsIndex() {
    assertEquals("index", controller.home());
  }

  @Test
  void login_returnsLogin() {
    assertEquals("login", controller.login());
  }

  @Test
  void register_returnsRegister() {
    assertEquals("register", controller.register());
  }

  @Test
  void profile_withNullPrincipal_returnsProfileAndNoAttributes() throws JsonProcessingException {
    Model model = new ConcurrentModel();
    String view = controller.profile(model, null);
    assertEquals("profile", view);
    assertNull(model.getAttribute("username"));
    assertNull(model.getAttribute("email"));
    assertNull(model.getAttribute("attributes"));
  }

  @Test
  void profile_withPrincipal_populatesModel() throws JsonProcessingException {
    OidcUser principal = Mockito.mock(OidcUser.class);
    when(principal.getPreferredUsername()).thenReturn("jdoe");
    when(principal.getEmail()).thenReturn("jdoe@example.com");
    Map<String, Object> attrs = Map.of("sub", "123", "name", "John Doe");
    when(principal.getAttributes()).thenReturn(attrs);

    Model model = new ConcurrentModel();
    String view = controller.profile(model, principal);

    assertEquals("profile", view);
    assertEquals("jdoe", model.getAttribute("username"));
    assertEquals("jdoe@example.com", model.getAttribute("email"));
    String attributes = (String) model.getAttribute("attributes");
    assertNotNull(attributes);
    // JSON should contain keys from the attributes map
    assertTrue(attributes.contains("\"sub\"") && attributes.contains("\"name\""));
  }

}
