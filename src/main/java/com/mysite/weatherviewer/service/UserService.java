package com.mysite.weatherviewer.service;

import com.mysite.weatherviewer.dto.RegisterDto;
import com.mysite.weatherviewer.dto.UserDto;
import com.mysite.weatherviewer.exception.UserAlreadyExistsException;
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
}
