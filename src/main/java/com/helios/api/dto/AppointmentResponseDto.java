package com.helios.api.dto;

import lombok.Data;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class AppointmentResponseDto {
    private Long appointmentId;
    private Boolean isAccepted;
    private Integer status;
    private Integer appointmentType;
    private String additionalMessage;
    private Date preferredDate;
    private Time preferredTime;
    private PatientDto patient;
    private LocalDateTime createDate;
}
