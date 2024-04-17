package com.helios.api.dto;

import lombok.Data;

@Data
public class PatientResponseDto {
    private Long patientId;
    private String firstName;
    private String lastName;
    private String email;
    private String contactNumber;
    private String remark;
    private Boolean isActive;
}
