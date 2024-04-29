package com.helios.api.service;

import com.helios.api.dto.ResponseDto;
import com.helios.api.entity.UserType;

public interface UserTypeService {
    UserType loadUserTypeById(Long userTypeId);
    UserType createUserType(String userType, int status);
    ResponseDto fetchUserTypes();
}
