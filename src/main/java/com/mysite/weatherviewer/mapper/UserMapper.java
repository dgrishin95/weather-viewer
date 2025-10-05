package com.mysite.weatherviewer.mapper;

import com.mysite.weatherviewer.dto.LoginDto;
import com.mysite.weatherviewer.dto.UserDto;
import com.mysite.weatherviewer.mapper.config.DefaultMapperConfig;
import com.mysite.weatherviewer.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = DefaultMapperConfig.class)
public interface UserMapper {
    UserDto toUserDto(User user);

    @Mapping(target = "login", source = "login")
    UserDto toUserDto(LoginDto loginDto);
}
