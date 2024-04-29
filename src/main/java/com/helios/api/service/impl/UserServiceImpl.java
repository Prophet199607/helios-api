package com.helios.api.service.impl;

import com.helios.api.dto.ResponseDto;
import com.helios.api.dto.UserDto;
import com.helios.api.dto.UserResponseDto;
import com.helios.api.entity.Role;
import com.helios.api.entity.User;
import com.helios.api.repository.UserRepository;
import com.helios.api.service.RoleService;
import com.helios.api.service.UserService;
import com.helios.api.util.ResponseType;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
    }

    @Override
    public User loadUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserResponseDto findUserByUserName(String userName) {
        return modelMapper.map(userRepository.findByUserName(userName), UserResponseDto.class);
    }

    @Override
    public User createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            return null;
        }

        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPassword);
        return userRepository.save(modelMapper.map(userDto, User.class));
    }

    @Override
    public void assignRoleToUser(Long userId, Long roleId) {
        User user = findUserById(userId);
        Role role = roleService.findByRoleId(roleId);
        user.assignRoleToUser(role);
    }
}
