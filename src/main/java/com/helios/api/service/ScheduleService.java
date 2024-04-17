package com.helios.api.service;

import com.helios.api.dto.ResponseDto;
import com.helios.api.dto.ScheduleDto;
import com.helios.api.entity.Schedule;

import java.util.List;

public interface ScheduleService {
    Schedule findScheduleById(Long scheduleId);

    ResponseDto createSchedule(ScheduleDto scheduleDto);

    ResponseDto removeSchedule(Long scheduleId);

    ResponseDto loadSchedulesByConsultant(Long scheduleId);

    ResponseDto loadAvailableSchedulesByConsultant(Long consultantId);

    void deleteAllSchedules(List<Schedule> schedules);

    Long getLastInertedScheduleId();

    Long getCountOfRecords();
}
