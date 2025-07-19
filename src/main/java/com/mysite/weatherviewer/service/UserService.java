package com.mysite.weatherviewer.service;

import com.mysite.weatherviewer.dto.RegisterDto;
import com.mysite.weatherviewer.mapper.UserMapper;
import com.mysite.weatherviewer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void register(RegisterDto registerDto) {

    }
}
