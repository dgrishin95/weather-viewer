package com.mysite.weatherviewer.mapper;

import com.mysite.weatherviewer.dto.RegisterDto;
import com.mysite.weatherviewer.dto.UserDto;
import com.mysite.weatherviewer.mapper.config.DefaultMapperConfig;
import com.mysite.weatherviewer.model.User;
import org.mapstruct.Mapper;

@Mapper(config = DefaultMapperConfig.class)
public interface UserMapper {
    User toEntity(RegisterDto registerDto);

    User toEntity(UserDto userDto);
    UserDto toUserDto(User user);
}
