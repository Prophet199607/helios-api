package com.helios.api.dto;

import lombok.Data;
@Data
public class DiagnosisDto {
    private Long diagnosisId;
    private String reportStatus;
    private Integer status;
    private String doctorRemark;
    private PatientDto patient;
    private AppointmentDto appointment;
}
