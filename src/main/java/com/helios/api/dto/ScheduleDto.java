package com.helios.api.dto;

import lombok.Data;

import java.sql.Date;
import java.sql.Time;

@Data
public class ScheduleDto {
    private Long scheduleId;
    private String title;
    private String start;
    private String end;
    private Date startDate;
    private Date endDate;
    private Time startTime;
    private Time endTime;
    private Boolean isBooked = false;
    private ConsultantResponseDto consultant;
}
