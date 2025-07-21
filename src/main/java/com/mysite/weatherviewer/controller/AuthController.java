package com.mysite.weatherviewer.controller;

import com.mysite.weatherviewer.dto.LoginDto;
import com.mysite.weatherviewer.dto.RegisterDto;
import com.mysite.weatherviewer.dto.SessionDto;
import com.mysite.weatherviewer.dto.UserDto;
import com.mysite.weatherviewer.service.SessionService;
import com.mysite.weatherviewer.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final SessionService sessionService;

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
    public String register(@ModelAttribute RegisterDto user, Model model, HttpServletResponse response) {
        UserDto newUser = userService.register(user);
        SessionDto newUserSession = sessionService.create(newUser);

        Cookie sessionCookie = new Cookie("session_id", newUserSession.getId().toString());
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);

        response.addCookie(sessionCookie);

//        sessionCookie.setMaxAge(...); // срок действия, в секундах

        model.addAttribute("user", user);
        return "auth/welcome";
    }
}
