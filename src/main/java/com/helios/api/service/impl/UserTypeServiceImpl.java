package com.helios.api.service.impl;

import com.helios.api.dto.UserTypeDto;
import com.helios.api.dto.ResponseDto;
import com.helios.api.entity.UserType;
import com.helios.api.repository.UserTypeRepository;
import com.helios.api.service.UserTypeService;
import com.helios.api.util.ResponseType;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserTypeServiceImpl implements UserTypeService {
    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserType loadUserTypeById(Long userTypeId) {
        return userTypeRepository.findById(userTypeId)
                .orElseThrow(() -> new EntityNotFoundException("User Type not found!"));
    }

    @Override
    public UserType createUserType(String userType, int status) {
        return userTypeRepository.save(new UserType(userType, status));
    }

    @Override
    public ResponseDto fetchUserTypes() {
        List<UserTypeDto> userTypes = userTypeRepository.findAll()
                .stream().map(country -> modelMapper.map(country, UserTypeDto.class))
                .toList();
        return new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "Success",
                userTypes
        );
    }
}
