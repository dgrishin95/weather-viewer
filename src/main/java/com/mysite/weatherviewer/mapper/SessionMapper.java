package com.mysite.weatherviewer.mapper;

import com.mysite.weatherviewer.dto.SessionDto;
import com.mysite.weatherviewer.mapper.config.DefaultMapperConfig;
import com.mysite.weatherviewer.model.Session;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DefaultMapperConfig.class)
public interface SessionMapper {

    @Mapping(target = "userId", expression = "java(session.getUser().getId())")
    SessionDto toSessionDto(Session session);
}
