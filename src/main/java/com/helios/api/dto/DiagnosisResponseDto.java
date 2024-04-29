package com.helios.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DiagnosisResponseDto {
    private Long diagnosisId;
    private String reportStatus;
    private Integer status;
    private String doctorRemark;
    private LocalDateTime createDate;
    private AppointmentDto appointment;
}
