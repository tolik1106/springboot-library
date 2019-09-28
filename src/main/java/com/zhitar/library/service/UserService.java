package com.zhitar.library.service;

import com.zhitar.library.dto.UserDto;

public interface UserService {

    UserDto findByEmail(String email);

    UserDto save(UserDto userDto);
}
