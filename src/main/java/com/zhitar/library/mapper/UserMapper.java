package com.zhitar.library.mapper;

import com.zhitar.library.domain.Book;
import com.zhitar.library.domain.User;
import com.zhitar.library.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class UserMapper implements EntityMapper<UserDto, User> {

    private final BookMapper bookMapper;
    @Override
    public User toEntity(UserDto userDto) {
        return new User(null, userDto.getName(), userDto.getEmail(), userDto.getPassword(), userDto.getPhone());
    }

    @Override
    public UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getPhone(), bookMapper.toDto(user.getBooks()));
    }

    @Override
    public List<User> toEntity(List<UserDto> dtos) {
        if (dtos == null) {
            return null;
        }
        List<User> users = new ArrayList<>();
        for (UserDto dto : dtos) {
            users.add(toEntity(dto));
        }
        return users;
    }

    @Override
    public List<UserDto> toDto(List<User> users) {
        if (users == null) {
            return null;
        }
        List<UserDto> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(toDto(user));
        }
        return dtos;
    }
}
