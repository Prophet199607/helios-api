package com.helios.api.service;

import com.helios.api.dto.UserDto;
import com.helios.api.dto.UserResponseDto;
import com.helios.api.entity.User;

public interface UserService {
    User findUserById(Long userId);
    User loadUserByEmail(String email);
    UserResponseDto findUserByUserName(String userName);
    User createUser(UserDto userDto);
    void assignRoleToUser(Long userId, Long roleId);

}
