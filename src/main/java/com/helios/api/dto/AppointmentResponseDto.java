package com.helios.api.dto;

import lombok.Data;

@Data
public class AppointmentResponseDto {
    private Long appointmentId;
    private Boolean isAccepted;
    private Integer status;
}
