package com.mysite.weatherviewer.integration;

import com.mysite.weatherviewer.config.TestConfig;
import com.mysite.weatherviewer.dto.RegisterDto;
import com.mysite.weatherviewer.dto.UserDto;
import com.mysite.weatherviewer.model.User;
import com.mysite.weatherviewer.repository.UserRepository;
import com.mysite.weatherviewer.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig(TestConfig.class)
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testRegister_Success() {
        RegisterDto registerDto = new RegisterDto("testuser", "password123");

        UserDto result = userService.register(registerDto);

        assertNotNull(result);
        assertEquals("testuser", result.getLogin());

        User userFromDb = userRepository.findByLogin("testuser");
        assertNotNull(userFromDb);
    }
}
