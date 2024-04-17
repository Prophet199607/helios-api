package com.helios.api.dto;

import lombok.Data;

@Data
public class NewJobSeekerRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String contactNumber;
    private String remark;
    private Boolean isActive;
    private UserDto user;
    private Boolean isAccepted;
    private Integer status;
    private ConsultantDto consultant;
    private PatientDto patient;
    private ScheduleDto schedule;
}
