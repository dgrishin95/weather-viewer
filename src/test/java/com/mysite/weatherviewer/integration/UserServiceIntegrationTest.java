package com.mysite.weatherviewer.integration;

import com.mysite.weatherviewer.config.TestConfig;
import com.mysite.weatherviewer.dto.LoginDto;
import com.mysite.weatherviewer.dto.RegisterDto;
import com.mysite.weatherviewer.dto.UserDto;
import com.mysite.weatherviewer.exception.InvalidCredentialsException;
import com.mysite.weatherviewer.exception.UserAlreadyExistsException;
import com.mysite.weatherviewer.model.User;
import com.mysite.weatherviewer.repository.UserRepository;
import com.mysite.weatherviewer.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringJUnitConfig(TestConfig.class)
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String TEST_LOGIN = "testuser";
    private static final String TEST_PASSWORD = "password123";

    @Test
    void testRegister_Success() {
        RegisterDto registerDto = new RegisterDto(TEST_LOGIN, TEST_PASSWORD);

        UserDto result = userService.register(registerDto);
        User userFromDb = userRepository.findByLogin(TEST_LOGIN);
        
        assertNotNull(result);
        assertEquals(TEST_LOGIN, result.getLogin());
        assertNotNull(userFromDb);
        assertTrue(passwordEncoder.matches(registerDto.getPassword(),
                userFromDb.getPassword()));
    }

    @Test
    void testRegister_DuplicateLogin_ThrowsException() {
        RegisterDto registerDto = new RegisterDto(TEST_LOGIN, TEST_PASSWORD);

        userService.register(registerDto);

        assertThrows(UserAlreadyExistsException.class, () -> userService.register(registerDto));
    }

    @Test
    void testLogin_Success() {
        RegisterDto registerDto = new RegisterDto(TEST_LOGIN, TEST_PASSWORD);
        UserDto register = userService.register(registerDto);

        LoginDto loginDto = new LoginDto(TEST_LOGIN, TEST_PASSWORD);
        UserDto login = userService.login(loginDto);

        assertEquals(register.getLogin(), login.getLogin());
        assertEquals(register.getId(), login.getId());
    }

    @Test
    void testLogin_WrongPassword_ThrowsException() {
        RegisterDto registerDto = new RegisterDto(TEST_LOGIN, TEST_PASSWORD);
        userService.register(registerDto);

        LoginDto loginDto = new LoginDto(TEST_LOGIN, "password1234");

        assertThrows(InvalidCredentialsException.class, () -> userService.login(loginDto));
    }

    @Test
    void testLogin_NonExistentUser_ThrowsException() {
        LoginDto loginDto = new LoginDto(TEST_LOGIN, "password1234");

        assertThrows(InvalidCredentialsException.class, () -> userService.login(loginDto));
    }
}
