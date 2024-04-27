package com.helios.api.dto;

import lombok.Data;

import java.util.Date;
import java.sql.Time;

@Data
public class AppointmentDto {
    private Long appointmentId;
    private Boolean isAccepted;
    private Integer status;
    private Date preferredDate;
    private Time preferredTime;
}
