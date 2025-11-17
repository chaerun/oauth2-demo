package com.chaerun.demo.oauth2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class WebController {

  private final ObjectMapper objectMapper;

  @GetMapping("/")
  public String home() {
    return "index";
  }

  @GetMapping("/profile")
  public String profile(Model model, @AuthenticationPrincipal OidcUser principal) throws JsonProcessingException {
    if (principal != null) {
      model.addAttribute("username", principal.getPreferredUsername());
      model.addAttribute("email", principal.getEmail());
      String attributes = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(principal.getAttributes());
      model.addAttribute("attributes", attributes);
    }
    return "profile";
  }

  @GetMapping("/login")
  public String login() {
    return "login";
  }

  @GetMapping("/register")
  public String register() {
    return "register";
  }
}

