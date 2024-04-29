package com.helios.api.dto;

import lombok.Data;

@Data
public class PatientDto {
    private Long patientId;
    private String firstName;
    private String lastName;
    private String email;
    private String contactNumber;
    private String nic;
    private String address1;
    private String address2;
    private String birthday;
    private String gender;
    private String remark;
    private Boolean isActive;
    private UserDto user;
}
