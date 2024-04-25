package com.helios.api.dto;

import lombok.Data;

@Data
public class StaffMemberResponseDto {
    private Long staffMemberId;
    private String firstName;
    private String lastName;
    private String email;
    private String contactNumber;
    private String remark;
    private Boolean isAvailable;
    private UserTypeDto userType;
}
