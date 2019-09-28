package com.zhitar.library.service.impl;

import com.zhitar.library.domain.Role;
import com.zhitar.library.domain.User;
import com.zhitar.library.dto.UserDto;
import com.zhitar.library.exception.NotFoundException;
import com.zhitar.library.mapper.EntityMapper;
import com.zhitar.library.mapper.UserMapper;
import com.zhitar.library.repository.RoleRepository;
import com.zhitar.library.repository.UserRepository;
import com.zhitar.library.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Service
@Repository
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("User with email " + email + " not found");
        }
        return userMapper.toDto(user);
    }

    @Override
    public UserDto save(UserDto userDto) {
        Role role = roleRepository.findByRole("USER");
        User user = userMapper.toEntity(userDto);
        user.setRoles(new HashSet<>(Collections.singletonList(role)));
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailWithRoles(email);

        if (user == null) {
            throw new UsernameNotFoundException("User with email " + email + " not found");
        }
        return user;
    }
}
