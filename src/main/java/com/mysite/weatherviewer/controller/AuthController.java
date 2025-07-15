package com.mysite.weatherviewer.controller;

import com.mysite.weatherviewer.dto.LoginDto;
import com.mysite.weatherviewer.dto.RegisterDto;
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
        model.addAttribute("user", new LoginDto());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDto user, Model model) {
        model.addAttribute("user", user);
        return "auth/welcome";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("new_user", new RegisterDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterDto user, Model model) {
        model.addAttribute("user", user);
        return "auth/welcome";
    }
}
