package com.mysite.weatherviewer.mapper;

import com.mysite.weatherviewer.dto.SessionDto;
import com.mysite.weatherviewer.mapper.config.DefaultMapperConfig;
import com.mysite.weatherviewer.model.Session;
import org.mapstruct.Mapper;

@Mapper(config = DefaultMapperConfig.class)
public interface SessionMapper {
    Session toEntity(SessionDto sessionDto);

    SessionDto toSessionDto(Session session);
}
