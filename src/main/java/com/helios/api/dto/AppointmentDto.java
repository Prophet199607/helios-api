package com.helios.api.dto;

import lombok.Data;

import java.util.Date;
import java.sql.Time;

@Data
public class AppointmentDto {
    private Long appointmentId;
    private Boolean isAccepted;
    private Integer status;
    private Integer appointmentType;
    private String additionalMessage;
    private Date preferredDate;
    private Time preferredTime;
    private PatientDto patient;
}
