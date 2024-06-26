package com.helios.api.controller;

import com.helios.api.dto.AppointmentDto;
import com.helios.api.dto.ResponseDto;
import com.helios.api.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> createConsultant(@RequestBody AppointmentDto appointmentDto) {
        ResponseDto responseDto = appointmentService.createAppointment(appointmentDto);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> loadAppointmentsByPatient(@PathVariable Long patientId) {
        ResponseDto responseDto = appointmentService.loadAppointmentsByPatient(patientId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

}
