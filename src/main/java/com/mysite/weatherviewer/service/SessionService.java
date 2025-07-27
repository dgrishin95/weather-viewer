package com.mysite.weatherviewer.service;

import com.mysite.weatherviewer.dto.SessionDto;
import com.mysite.weatherviewer.dto.UserDto;
import com.mysite.weatherviewer.mapper.SessionMapper;
import com.mysite.weatherviewer.mapper.UserMapper;
import com.mysite.weatherviewer.model.Session;
import com.mysite.weatherviewer.model.User;
import com.mysite.weatherviewer.repository.SessionRepository;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;
    private final UserMapper userMapper;

    @Transactional
    public SessionDto create(UserDto newUser) {
        Session session = Session.builder()
                .id(UUID.randomUUID())
                .user(new User(newUser.getId(), newUser.getLogin()))
                .expiresAt(Instant.now().plus(Duration.ofMinutes(1)))
                .build();

        sessionRepository.save(session);

        return sessionMapper.toSessionDto(session);
    }

    @Transactional
    public void remove(String foundUuid) {
        sessionRepository.remove(UUID.fromString(foundUuid));
    }
}
