package com.helios.api.controller;

import com.helios.api.dto.ResponseDto;
import com.helios.api.dto.ScheduleDto;
import com.helios.api.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CONSULTANT')")
    public ResponseEntity<ResponseDto> createConsultant(@RequestBody ScheduleDto scheduleDto) {
        ResponseDto responseDto = scheduleService.createSchedule(scheduleDto);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @GetMapping("/all/consultant/{consultantId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CONSULTANT')")
    public ResponseEntity<ResponseDto> loadSchedulesByConsultant(@PathVariable Long consultantId) {
        ResponseDto responseDto = scheduleService.loadSchedulesByConsultant(consultantId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @GetMapping("/available/consultant/{consultantId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CONSULTANT', 'ROLE_USER')")
    public ResponseEntity<ResponseDto> loadAvailableSchedulesByConsultant(@PathVariable Long consultantId) {
        ResponseDto responseDto = scheduleService.loadAvailableSchedulesByConsultant(consultantId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @DeleteMapping("/{scheduleId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CONSULTANT')")
    public ResponseEntity<ResponseDto> deleteSchedule(@PathVariable Long scheduleId) {
        ResponseDto responseDto = scheduleService.removeSchedule(scheduleId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }
}
