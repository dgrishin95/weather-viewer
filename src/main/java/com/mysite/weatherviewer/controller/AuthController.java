package com.mysite.weatherviewer.controller;

import com.mysite.weatherviewer.dto.AuthDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("new_user", new AuthDto());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute AuthDto user, Model model) {
        model.addAttribute("new_user", user);
        return "auth/welcome";
    }
}
