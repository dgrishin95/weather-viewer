package com.mysite.weatherviewer.service;

import com.mysite.weatherviewer.dto.RegisterDto;
import com.mysite.weatherviewer.dto.UserDto;
import com.mysite.weatherviewer.mapper.UserMapper;
import com.mysite.weatherviewer.model.User;
import com.mysite.weatherviewer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDto register(RegisterDto registerDto) {
        UserDto newUserDto = new UserDto();

        if (userRepository.existsByLogin(registerDto.getLogin())) {
            String encodedPassword = passwordEncoder.encode(registerDto.getPassword());

            User newUser = User.builder()
                    .login(registerDto.getLogin())
                    .password(encodedPassword)
                    .build();

            User savedUser = userRepository.save(newUser);

            newUserDto.setId(savedUser.getId());
            newUserDto.setLogin(savedUser.getLogin());
        }
        else {
            // TODO: catch Exception
        }

        return newUserDto;
    }
}
