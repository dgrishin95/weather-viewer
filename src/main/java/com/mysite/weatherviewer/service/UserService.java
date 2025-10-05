package com.mysite.weatherviewer.service;

import com.mysite.weatherviewer.dto.LoginDto;
import com.mysite.weatherviewer.dto.RegisterDto;
import com.mysite.weatherviewer.dto.UserDto;
import com.mysite.weatherviewer.exception.UserAlreadyExistsException;
import com.mysite.weatherviewer.exception.InvalidCredentialsException;
import com.mysite.weatherviewer.mapper.UserMapper;
import com.mysite.weatherviewer.model.User;
import com.mysite.weatherviewer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto register(RegisterDto registerDto) {
        if (userRepository.existsByLogin(registerDto.getLogin())) {
            throw new UserAlreadyExistsException("User with login already exists");
        }

        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());

        User newUser = User.builder()
                .login(registerDto.getLogin())
                .password(encodedPassword)
                .build();

        User savedUser = userRepository.save(newUser);

        return userMapper.toUserDto(savedUser);
    }

    @Transactional(readOnly = true)
    public UserDto login(LoginDto loginDto) {
        User foundUser = userRepository.findByLogin(loginDto.getLogin());

        if (foundUser == null ||
                !passwordEncoder.matches(loginDto.getPassword(), foundUser.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        return userMapper.toUserDto(foundUser);
    }

    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        User foundUser = userRepository.findById(id);

        return userMapper.toUserDto(foundUser);
    }
}
