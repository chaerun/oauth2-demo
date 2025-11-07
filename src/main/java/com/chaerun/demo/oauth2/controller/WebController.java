package com.chaerun.demo.oauth2.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String getHomePage() {
        return "index";
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            model.addAttribute("username", principal.getAttribute("preferred_username"));
            model.addAttribute("email", principal.getAttribute("email"));
        }
        return "profile";
    }
}

