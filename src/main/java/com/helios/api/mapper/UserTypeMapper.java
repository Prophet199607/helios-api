package com.helios.api.mapper;

import com.helios.api.dto.UserTypeDto;
import com.helios.api.entity.UserType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserTypeMapper {
    public UserTypeDto fromUserType(UserType userType) {
        UserTypeDto userTypeDto = new UserTypeDto();
        BeanUtils.copyProperties(userType, userTypeDto);
        return userTypeDto;
    }

    public UserType fromUserTypeDto(UserTypeDto userTypeDto) {
        UserType userType = new UserType();
        BeanUtils.copyProperties(userTypeDto, userType);
        return userType;
    }
}
