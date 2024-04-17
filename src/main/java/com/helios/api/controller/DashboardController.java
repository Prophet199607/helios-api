package com.helios.api.controller;

import com.helios.api.dto.ResponseDto;
import com.helios.api.service.AppointmentService;
import com.helios.api.service.ConsultantService;
import com.helios.api.service.PatientService;
import com.helios.api.service.ScheduleService;
import com.helios.api.util.ResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/dashboard")
public class DashboardController {

    @Autowired
    private ConsultantService consultantService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ScheduleService scheduleService;
    @GetMapping("/admin")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> getFiguresForAdmin() {
        Long totalConsultants = consultantService.getCountOfRecords();
        Long totalJobSeekers = patientService.getCountOfRecords();
        Long totalAppointments = appointmentService.getCountOfRecords();
        Long totalSchedules = scheduleService.getCountOfRecords();

        Map<String, Long> hashMap = new HashMap<>();
        hashMap.put("totalConsultants", totalConsultants);
        hashMap.put("totalJobSeekers", totalJobSeekers);
        hashMap.put("totalAppointments", totalAppointments);
        hashMap.put("totalSchedules", totalSchedules);

        ResponseDto responseDto = new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "Success!",
                hashMap
        );

        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @GetMapping("/consultant/{consultantId}")
    @PreAuthorize("hasAnyAuthority('ROLE_CONSULTANT')")
    public ResponseEntity<ResponseDto> getFiguresForConsultant(@PathVariable Long consultantId) {
        Long myScheduleCount = consultantService.getScheduleCount(consultantId);
        Long myAppointmentsCount = consultantService.getAppointmentsCount(consultantId);
        Long myNewAppointmentsCount = consultantService.getNewAppointmentsCount(consultantId);

        Map<String, Long> hashMap = new HashMap<>();
        hashMap.put("myScheduleCount", myScheduleCount);
        hashMap.put("myAppointmentsCount", myAppointmentsCount);
        hashMap.put("myNewAppointmentsCount", myNewAppointmentsCount);

        ResponseDto responseDto = new ResponseDto(
                ResponseType.SUCCESS,
                HttpStatus.OK,
                "Success!",
                hashMap
        );

        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }
}
