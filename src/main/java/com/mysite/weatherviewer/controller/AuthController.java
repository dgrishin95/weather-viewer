package com.mysite.weatherviewer.controller;

import com.mysite.weatherviewer.dto.LoginDto;
import com.mysite.weatherviewer.dto.RegisterDto;
import com.mysite.weatherviewer.dto.SessionDto;
import com.mysite.weatherviewer.dto.UserDto;
import com.mysite.weatherviewer.exception.InvalidCredentialsException;
import com.mysite.weatherviewer.exception.InvalidUserDataException;
import com.mysite.weatherviewer.exception.UserAlreadyExistsException;
import com.mysite.weatherviewer.service.SessionService;
import com.mysite.weatherviewer.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.session.cookie.name}")
    private String cookieName;

    @Value("${app.session.cookie.path}")
    private String cookiePath;

    @Value("${app.session.cookie.http-only}")
    private boolean cookieHttpOnly;

    @Value("${app.session.cookie.expire-now}")
    private int expireNow;

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
            setSessionCookie(response, newUserSession);

            model.addAttribute("user", user);
            return "auth/welcome";
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
            setSessionCookie(response, newUserSession);

            model.addAttribute("user", user);
            return "auth/welcome";
        } catch (UserAlreadyExistsException | InvalidUserDataException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            return "redirect:/auth/register";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Optional<Cookie> foundCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst();

        foundCookie.ifPresent(cookie -> removeSessionCookie(cookie, response));

        return "redirect:/auth/login";
    }

    private void setSessionCookie(HttpServletResponse response, SessionDto newUserSession) {
        Cookie newCookie = new Cookie(cookieName, newUserSession.getId().toString());
        newCookie.setPath(cookiePath);
        newCookie.setHttpOnly(cookieHttpOnly);

        response.addCookie(newCookie);
    }

    private void removeSessionCookie(Cookie foundCookie, HttpServletResponse response) {
        sessionService.remove(foundCookie.getValue());
        foundCookie.setPath(cookiePath);
        foundCookie.setMaxAge(expireNow);

        response.addCookie(foundCookie);
    }
}
