package com.helios.api.mapper;

import com.helios.api.dto.StaffMemberDto;
import com.helios.api.entity.StaffMember;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class StaffMemberMapper {
    public StaffMemberDto fromStaffMember(StaffMember staffMember) {
        StaffMemberDto staffMemberDto = new StaffMemberDto();
        BeanUtils.copyProperties(staffMember, staffMemberDto);
        return staffMemberDto;
    }

    public StaffMember fromStaffMemberDto(StaffMemberDto staffMemberDto) {
        StaffMember staffMember = new StaffMember();
        BeanUtils.copyProperties(staffMemberDto, staffMember);
        return staffMember;
    }
}
