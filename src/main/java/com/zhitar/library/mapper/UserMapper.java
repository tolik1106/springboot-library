package com.zhitar.library.mapper;

import com.zhitar.library.domain.User;
import com.zhitar.library.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements EntityMapper<UserDto, User> {
    @Override
    public User toEntity(UserDto userDto) {
        return new User(null, userDto.getName(), userDto.getEmail(), userDto.getPassword(), userDto.getPhone());
    }

    @Override
    public UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getPhone());
    }
}
