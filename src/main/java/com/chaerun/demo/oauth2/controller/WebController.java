package com.chaerun.demo.oauth2.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

  @GetMapping("/")
  public String home() {
    return "index";
  }

  @GetMapping("/profile")
  public String profile(Model model, @AuthenticationPrincipal OidcUser principal) {
    if (principal != null) {
      model.addAttribute("username", principal.getPreferredUsername());
      model.addAttribute("email", principal.getEmail());
      model.addAttribute("attributes", principal.getAttributes());
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

