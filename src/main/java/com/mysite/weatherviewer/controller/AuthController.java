package com.mysite.weatherviewer.controller;

import com.mysite.weatherviewer.dto.LoginDto;
import com.mysite.weatherviewer.dto.RegisterDto;
import com.mysite.weatherviewer.dto.SessionDto;
import com.mysite.weatherviewer.dto.UserDto;
import com.mysite.weatherviewer.exception.InvalidCredentialsException;
import com.mysite.weatherviewer.exception.InvalidUserDataException;
import com.mysite.weatherviewer.exception.UserAlreadyExistsException;
import com.mysite.weatherviewer.service.CookieService;
import com.mysite.weatherviewer.service.SessionService;
import com.mysite.weatherviewer.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final SessionService sessionService;
    private final CookieService cookieService;

    @GetMapping("/welcome")
    public String welcome() {
        return "auth/welcome";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new LoginDto());
        return "auth/login";
    }

    @PostMapping("/login")
    public String handleLogin(@ModelAttribute LoginDto user,
                              Model model,
                              RedirectAttributes redirectAttributes,
                              HttpServletResponse response) {
        try {
            UserDto foundUser = userService.login(user);
            SessionDto newUserSession = sessionService.create(foundUser);
            cookieService.addSessionCookie(response, newUserSession);

            model.addAttribute("user", user);
            return "redirect:/auth/welcome";
        } catch (InvalidCredentialsException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        return "auth/register";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute RegisterDto user,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes,
                                 HttpServletResponse response) {
        try {
            if (bindingResult.hasErrors()) {
                String errors = bindingResult.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.joining(", "));
                throw new InvalidUserDataException("Validation failed: " + errors);
            }

            UserDto newUser = userService.register(user);

            SessionDto newUserSession = sessionService.create(newUser);
            cookieService.addSessionCookie(response, newUserSession);

            model.addAttribute("user", user);
            return "redirect:/auth/welcome";
        } catch (UserAlreadyExistsException | InvalidUserDataException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            return "redirect:/auth/register";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Optional<Cookie> foundCookie = cookieService.findSessionCookie(request);

        foundCookie.ifPresent(cookie -> {
            sessionService.remove(cookie.getValue());
            cookieService.removeSessionCookie(cookie, response);
        });

        return "redirect:/auth/login";
    }
}
