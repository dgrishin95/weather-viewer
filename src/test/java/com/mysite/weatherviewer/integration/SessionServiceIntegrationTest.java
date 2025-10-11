package com.mysite.weatherviewer.integration;

import com.mysite.weatherviewer.config.TestConfig;
import com.mysite.weatherviewer.dto.RegisterDto;
import com.mysite.weatherviewer.dto.SessionDto;
import com.mysite.weatherviewer.dto.UserDto;
import com.mysite.weatherviewer.mapper.SessionMapper;
import com.mysite.weatherviewer.model.Session;
import com.mysite.weatherviewer.repository.SessionRepository;
import com.mysite.weatherviewer.service.SessionService;
import com.mysite.weatherviewer.service.UserService;
import jakarta.persistence.NoResultException;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringJUnitConfig(TestConfig.class)
@Transactional
class SessionServiceIntegrationTest {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionMapper sessionMapper;

    @Autowired
    private UserService userService;

    @Value("${app.session.duration.minutes}")
    private int durationMinutes;

    private static final String TEST_LOGIN = "testuser";
    private static final String TEST_PASSWORD = "password123";

    @Test
    void testCreateSession_Success() {
        SessionDto sessionDto = createUser();

        Session session = sessionRepository.findByUuid(sessionDto.getId());

        assertEquals(sessionDto.getId(), session.getId());
        assertEquals(sessionDto.getExpiresAt(), session.getExpiresAt());
    }

    @Test
    void testIsSessionActive_ActiveSession() {
        SessionDto sessionDto = createUser();

        assertTrue(sessionService.isSessionActive(sessionDto));
    }

    @Test
    void testIsSessionActive_ExpiredSession() {
        SessionDto sessionDto = createUser();

        Session session = sessionRepository.findByUuid(sessionDto.getId());
        session.setExpiresAt(session.getExpiresAt().minus(Duration.ofMinutes(durationMinutes)));
        Session saved = sessionRepository.save(session);

        SessionDto savedDto = sessionMapper.toSessionDto(saved);

        assertFalse(sessionService.isSessionActive(savedDto));
    }

    @Test
    void testRemoveSession_Success() {
        SessionDto sessionDto = createUser();

        sessionService.remove(sessionDto.getId().toString());

        assertThrows(NoResultException.class, () -> sessionRepository.findByUuid(sessionDto.getId()));
    }

    private SessionDto createUser() {
        RegisterDto registerDto = new RegisterDto(TEST_LOGIN, TEST_PASSWORD);
        UserDto userDto = userService.register(registerDto);
        return sessionService.create(userDto);
    }
}
