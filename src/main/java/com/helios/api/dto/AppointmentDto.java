package com.helios.api.dto;
import lombok.Data;

@Data
public class AppointmentDto {
    private Long appointmentId;
    private Boolean isAccepted;
    private Integer status;
    private ConsultantDto consultant;
    private PatientDto patientDto;
    private ScheduleDto schedule;
}
